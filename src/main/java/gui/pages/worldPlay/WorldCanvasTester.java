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
import world.HostWorld;
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
        HumanPlayer player = new HumanPlayer(user.getName());
        
        player.applyBuild(Settings.getDataSet().getDefaultBuild());
        
        HostWorld world = new HostWorld(new ServerProvider().createHost(), WorldContent.createDefaultBattle());
        Team t1 = new Team("Test", Color.BLUE);
        Team t2 = Team.constructRandomTeam("Rando", Color.yellow, 1, 1);
        t1.addMember(player);
        world.setPlayerTeam(t1).setEnemyTeam(t2);
        
        Battle b = new Battle(10, 5);
        b.setHost(world.getContent());
        world.setCurrentMinigame(b);
        world.init();
        
        WorldCanvas canvas = new WorldCanvas(world);
        canvas.addPlayerControls(new PlayerControls(world, player.id, orpheus));
        canvas.setPauseEnabled(true);
        MainWindow mw = MainWindow.getInstance();
        WorldPage wp = new WorldPage(orpheus);
        wp.setCanvas(canvas);
        mw.switchToPage(wp);
        
        user.setRemotePlayerId(player.id);
        
        
        //now to try serializing it...
        String serial = world.getContent().serializeToString();
        
        WorldContent newContent = WorldContent.fromSerializedString(serial);
        world.setContent(newContent);
        newContent.setShell(world);
        
        //user.setPlayer((HumanPlayer)newContent.getPlayerTeam().getMemberById(user.getRemotePlayerId()));
        
        //newWorld.init();
        
        wp = new WorldPage(new SoloOrpheusCommandInterpreter(user));
        wp.setCanvas(canvas);
        mw.switchToPage(wp);
        canvas.setPauseEnabled(true);
        world.getCanvas().registerKey(KeyEvent.VK_S, true, ()->{
            Team t = world.getPlayerTeam();
            System.out.println("Total entities to serialize: " + t.length());
            String s = SerialUtil.serializeToString(t);
            Team tClone = (Team)SerialUtil.fromSerializedString(s);
            System.out.println("Total entities deserialized: " + tClone.length());
        });
        
        
        
        ObjectOutputStream out = new ObjectOutputStream(System.out);
        String ser = null;
        for(int i = 0; i < 1000000; i++){
            world.getContent().serializeToString();
            out.writeObject(ser);
            //out.reset();
            //WorldContent deser = WorldContent.fromSerializedString(ser);
            //world.setContent(deser);
            if(i % 10000 == 0){
                System.out.println(i);
            }
        }
        out.close();
    }
}
