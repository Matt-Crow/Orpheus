package entities;

import controllers.Master;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import net.ServerMessage;
import net.ServerMessageType;
import windows.Canvas;
import windows.world.WorldCanvas;

/**
 *
 * @author Matt Crow
 */
public class PlayerControls implements MouseListener{
    private final Player p;
    private boolean isRemote;
    private String receiverIpAddr;
    
    public PlayerControls(Player forPlayer, boolean remote){
        p = forPlayer;
        isRemote = remote;
        if(isRemote){
            receiverIpAddr = p.getWorld().getHostIp();
        }else{
            receiverIpAddr = null;
        }
    }
    
    public PlayerControls(Player forPlayer){
        this(forPlayer, false);
    }
    
    private void useMelee(){
        if(isRemote){
            Master.getServer().send(
                new ServerMessage(
                    turnToMouseString() + "\n use melee",
                    ServerMessageType.CONTROL_PRESSED
                ), 
                receiverIpAddr
            );
        }else{
            turnToMouse();
            p.useMeleeAttack();
        }
    }
    private void useAtt(int i){
        if(isRemote){
            Master.getServer().send(
                new ServerMessage(
                    turnToMouseString() + "\n use " + i,
                    ServerMessageType.CONTROL_PRESSED
                ), 
                receiverIpAddr
            );
        } else {
            turnToMouse();
            p.useAttack(i);
        }
    }
    
    public void registerControlsTo(Canvas plane){
        plane.registerKey(KeyEvent.VK_Q, true, ()->{
            useMelee();
        });
        plane.registerKey(KeyEvent.VK_1, true, ()->{
            useAtt(0);
        });
        plane.registerKey(KeyEvent.VK_2, true, ()->{
            useAtt(1);
        });
        plane.registerKey(KeyEvent.VK_3, true, ()->{
            useAtt(2);
        });
    }
    
    private void turnToMouse(){
        WorldCanvas c = p.getWorld().getCanvas();
        p.turnTo(c.getMouseX(), c.getMouseY());
    }
    private String turnToMouseString(){
        WorldCanvas c = p.getWorld().getCanvas();
        return String.format("turn to %d, %d", c.getMouseX(), c.getMouseY());
    }
    private static void decodeTurnToMouseString(Player p, String s){
        String coords = s.replace("turn to ", "").trim();
        String[] split = coords.split(",");
        int x = Integer.parseInt(split[0].trim());
        int y = Integer.parseInt(split[1].trim());
        p.turnTo(x, y);
    }
    
    /**
     * Decodes the body of a server message sent by
     * this class, and applies the changes contained
     * in it to the given player
     * 
     * @param p
     * @param s 
     */
    public static void decode(Player p, String s){
        for(String str : s.split("\n")){
            if(str.contains("turn to")){
                decodeTurnToMouseString(p, str);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if(isRemote){
            Master.getServer().send(
                new ServerMessage(
                    "toggle following mouse",
                    ServerMessageType.CONTROL_PRESSED
                ), 
                receiverIpAddr
            );
        } else {
            boolean b = p.getFollowingMouse();
            p.setFollowingMouse(!b);
            p.setMoving(!b);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
