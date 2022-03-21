package gui.pages.worldPlay;

import controls.PlayerControls;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import start.MainWindow;
import start.SoloOrpheusCommandInterpreter;
import users.LocalUser;
import util.SerialUtil;
import util.Settings;
import world.TempWorld;
import world.TempWorldBuilder;
import world.WorldImpl;
import world.WorldBuilder;
import world.WorldContent;
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
        WorldBuilder wb = new WorldBuilder();
        
        TempWorld entireWorld = builder.build();
        WorldContent world = entireWorld.getContent();
        WorldImpl realWorld = wb.build();
        
        
        HumanPlayer player = new HumanPlayer(entireWorld, user.getName());
        
        player.applyBuild(Settings.getDataSet().getDefaultBuild());
        
        Team t1 = new Team("Test", Color.BLUE);
        Team t2 = Team.constructRandomTeam(entireWorld, "Rando", Color.yellow, 1, 1);
        t1.addMember(player);
        entireWorld.getContent().setPlayerTeam(t1);
        entireWorld.getContent().setAITeam(t2);
        
        entireWorld.init();
        
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
        String serial = SerialUtil.serializeToString(world);
        
        WorldContent newContent = (WorldContent)SerialUtil.fromSerializedString(serial);
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
                ser = SerialUtil.serializeToString(entireWorld.getContent());
                
                WorldContent deser = (WorldContent) SerialUtil.fromSerializedString(ser);
                entireWorld.setContent(deser);
                if(i % 10000 == 0){
                    System.out.println(i);
                }
            }
        }
    }
}
