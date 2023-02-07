package orpheus.client.gui.pages;

import orpheus.client.gui.pages.play.WorldCanvas;
import orpheus.client.gui.pages.play.WorldPage;
import java.awt.Color;
import java.awt.event.KeyEvent;

import orpheus.client.AppContext;
import orpheus.client.gui.components.ComponentFactory;
import serialization.WorldSerializer;
import start.*;
import users.LocalUser;
import util.Settings;
import world.*;
import world.battle.Team;
import world.entities.HumanPlayer;
import world.game.*;

/**
 * Used to test the world canvas without going through the GUI as much
 * @author Matt Crow
 */
public class WorldCanvasTester {

    private static final boolean GUI = !false;
    private static final boolean BATCH_SERIALIZE = false;

    public static void main(String[] args) {
        WorldBuilder wb = new WorldBuilderImpl();

        Team players = new Team("Test", Color.BLUE);
        World w = wb
                .withGame(new Onslaught(5))
                .withPlayers(players)
                .withAi(Team.constructRandomTeam(null, "Rando", Color.yellow, 1, 1))
                .build();

        LocalUser user = LocalUser.getInstance();
        HumanPlayer player = new HumanPlayer(w, user.getName());
        player.applyBuild(Settings.getDataSet().getDefaultBuild());
        players.addMember(player);
        user.setRemotePlayerId(player.id);

        w.init();

        WorldSerializer ws = new WorldSerializer(w);

        if (GUI) {
            SoloOrpheusCommandInterpreter orpheus = new SoloOrpheusCommandInterpreter(user);

            WorldCanvas canvas = new WorldCanvas(
                    w,
                    new PlayerControls(w, player.id, orpheus),
                    true
            );
            
            var settings = new Settings();
            var context = new AppContext(settings);
            ComponentFactory cf = new ComponentFactory();
            PageController mw = new PageController(context, cf);
            WorldPage wp = new WorldPage(context, mw, cf);
            wp.setCanvas(canvas);
            mw.switchToPage(wp);
            canvas.start();

            canvas.registerKey(KeyEvent.VK_R, true, () -> {
                long start = System.currentTimeMillis();
                String s = ws.serializeToString();
                long middle = System.currentTimeMillis();
                ws.deserialize(s);
                long end = System.currentTimeMillis();
                System.out.printf("took %d ms to serialize, %d ms to deserialize\n", middle - start, end - middle);
            });
        }

        if (BATCH_SERIALIZE) {
            try {
                String ser = null;
                long start = System.currentTimeMillis();
                for (int i = 0; i < 1000; i++) {
                    ser = ws.serializeToString();
                    ws.deserialize(ser);
                }
                long end = System.currentTimeMillis();
                System.out.printf(
                        "Took %d ms to serialize-deserialize 1000 times\n",
                        end - start
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
