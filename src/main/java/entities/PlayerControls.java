package entities;

import controllers.Master;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import net.ServerMessage;
import net.ServerMessageType;
import windows.Canvas;
import windows.EndOfFrameListener;
import windows.world.WorldCanvas;

/**
 *
 * @author Matt Crow
 */
public class PlayerControls implements MouseListener, EndOfFrameListener{
    private final AbstractPlayer p;
    private boolean isRemote;
    private String receiverIpAddr;
    
    public PlayerControls(AbstractPlayer forPlayer, boolean remote){
        p = forPlayer;
        isRemote = remote;
        if(isRemote){
            receiverIpAddr = p.getWorld().getHostIp();
        }else{
            receiverIpAddr = null;
        }
    }
    
    public PlayerControls(AbstractPlayer forPlayer){
        this(forPlayer, false);
    }
    
    private void useMelee(){
        String msg = "turn to " + mouseString() + "\n use melee";
        if(isRemote){
            Master.SERVER.send(
                new ServerMessage(
                    msg,
                    ServerMessageType.CONTROL_PRESSED
                ), 
                receiverIpAddr
            );
        }else{
            decode(p, msg);
        }
    }
    private void useAtt(int i){
        String msg = "turn to " + mouseString() + "\n use " + i;
        if(isRemote){
            Master.SERVER.send(
                new ServerMessage(
                    msg,
                    ServerMessageType.CONTROL_PRESSED
                ), 
                receiverIpAddr
            );
        } else {
            decode(p, msg);
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
    public static void decode(AbstractPlayer p, String s){
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
                int num = Integer.parseInt(str.substring(str.indexOf("use") + 3).trim());
                p.useAttack(num);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        p.setFollowingMouse(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        p.setFollowingMouse(false);
    }
    
    @Override
    public void frameEnded(Canvas c) {
        if(p.getFollowingMouse()){
            String msg = "move to " + mouseString();
            if(isRemote){
                Master.SERVER.send(
                    new ServerMessage(
                        msg,
                        ServerMessageType.CONTROL_PRESSED
                    ), 
                    receiverIpAddr
                );
            } else {
                decode(p, msg);
            }
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
