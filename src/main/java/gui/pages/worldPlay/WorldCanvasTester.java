package gui.pages.worldPlay;

import controls.PlayerControls;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import net.ServerProvider;
import start.MainWindow;
import start.SoloOrpheusCommandInterpreter;
import users.LocalUser;
import util.SerialUtil;
import util.Settings;
import world.AbstractWorldShell;
import world.HostWorld;
import world.TempWorld;
import world.TempWorldBuilder;
import world.WorldContent;
import world.battle.Battle;
import world.battle.Team;
import world.entities.HumanPlayer;

/**
 *
 * @author Matt Crow
 */
public class WorldCanvasTester {
    public static void main(String[] args) throws IOException{
        LocalUser user = LocalUser.getInstance();
        SoloOrpheusCommandInterpreter orpheus = new SoloOrpheusCommandInterpreter(user);
        
        TempWorldBuilder builder = new TempWorldBuilder();
        
        AbstractWorldShell shell = new HostWorld(new ServerProvider().createHost());
        TempWorld entireWorld = builder.withShell(shell).build();
        WorldContent world = entireWorld.getContent();
        
        
        HumanPlayer player = new HumanPlayer(entireWorld.getContent(), user.getName());
        
        player.applyBuild(Settings.getDataSet().getDefaultBuild());
        
        Team t1 = new Team("Test", Color.BLUE);
        Team t2 = Team.constructRandomTeam(entireWorld.getContent(), "Rando", Color.yellow, 1, 1);
        t1.addMember(player);
        entireWorld.getContent().setPlayerTeam(t1);
        entireWorld.getContent().setAITeam(t2);
        
        Battle b = new Battle(10, 5);
        b.setHost(entireWorld.getContent());
        world.setMinigame(b);
        world.init();
        
        WorldCanvas canvas = new WorldCanvas(
            entireWorld, 
            new PlayerControls(entireWorld, player.id, orpheus),
            true
        );
        
        MainWindow mw = MainWindow.getInstance();
        WorldPage wp = new WorldPage(orpheus);
        wp.setCanvas(canvas);
        mw.switchToPage(wp);
        canvas.start();
        
        user.setRemotePlayerId(player.id);
        
        
        //now to try serializing it...
        String serial = world.serializeToString();
        
        WorldContent newContent = WorldContent.fromSerializedString(serial);
        entireWorld.setContent(newContent);
        
        wp = new WorldPage(new SoloOrpheusCommandInterpreter(user));
        wp.setCanvas(canvas);
        mw.switchToPage(wp);
        
        canvas.registerKey(KeyEvent.VK_S, true, ()->{
            Team t = world.getPlayers();
            System.out.println("Total entities to serialize: " + t.length());
            String s = SerialUtil.serializeToString(t);
            Team tClone = (Team)SerialUtil.fromSerializedString(s);
            System.out.println("Total entities deserialized: " + tClone.length());
        });
        
        
        try (ObjectOutputStream out = new ObjectOutputStream(System.out)) {
            String ser = null;
            for(int i = 0; i < 1000000; i++){
                entireWorld.getContent().serializeToString();
                out.writeObject(ser);
                //out.reset();
                //WorldContent deser = WorldContent.fromSerializedString(ser);
                //world.setContent(deser);
                if(i % 10000 == 0){
                    System.out.println(i);
                }
            }
        }
    }
}
