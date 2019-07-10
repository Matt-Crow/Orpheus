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
                    "turn to " + mouseString() + "\n use melee",
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
                    "turn to " + mouseString() + "\n use " + i,
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
    private String mouseString(){
        WorldCanvas c = p.getWorld().getCanvas();
        return String.format("(%d, %d)", c.getMouseX(), c.getMouseY());
    }
    private static int[] decodeMouseString(String s){
        String coords = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
        String[] split = coords.split(",");
        int x = Integer.parseInt(split[0].trim());
        int y = Integer.parseInt(split[1].trim());
        return new int[]{x, y};
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
        int[] coords;
        for(String str : s.split("\n")){
            if(str.contains("turn to")){
                coords = decodeMouseString(str);
                p.turnTo(coords[0], coords[1]);
            }
            if(str.contains("move to")){
                coords = decodeMouseString(str);
                p.setPath(coords[0], coords[1]);
            }
            if(str.contains("use melee")){
                p.useMeleeAttack();
            } else if(str.contains("use")){
                int num = Integer.parseInt(str.substring(str.indexOf("use")).trim());
                p.useAttack(num);
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
                    "move to " + mouseString(),
                    ServerMessageType.CONTROL_PRESSED
                ), 
                receiverIpAddr
            );
        } else {
            p.moveToMouse();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
