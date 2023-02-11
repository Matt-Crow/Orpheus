package orpheus.client.gui.pages;

import orpheus.client.gui.pages.play.WorldCanvas;
import orpheus.client.gui.pages.play.WorldPage;
import java.awt.Color;
import java.awt.event.KeyEvent;

import orpheus.client.ClientAppContext;
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
        var settings = new Settings();
        var cf = new ComponentFactory();
        var context = new ClientAppContext(settings, cf);
        
        var wb = new WorldBuilderImpl();

        var players = new Team("Test", Color.BLUE);
        var w = wb
                .withGame(new Onslaught(5))
                .withPlayers(players)
                .withAi(Team.constructRandomTeam(null, "Rando", Color.yellow, 1, 1))
                .build();

        var user = LocalUser.getInstance();
        var player = new HumanPlayer(w, user.getName());
        var ds = context.getDataSet();
        player.applyBuild(ds.assemble(ds.getDefaultBuild()));
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
            
            PageController mw = new PageController(context);
            WorldPage wp = new WorldPage(context, mw);
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
