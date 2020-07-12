package controls;

import entities.HumanPlayer;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import windows.Canvas;
import windows.EndOfFrameListener;
import windows.world.WorldCanvas;
import world.AbstractWorld;

/**
 * CONTROLS:<br>
 * - click to move to the mouse cursor<br>
 * - Q to use melee attack
 * - 1-3 to use active abilities 1-3 respectively<br>
 * 
 * I am definitely going to want to change this later,
 * as I don't feel these Runescapey controls fit the
 * fast paced theme of the game.
 * 
 * @author Matt Crow
 */
public abstract class AbstractPlayerControls extends AbstractControlScheme<HumanPlayer> implements MouseListener, EndOfFrameListener{
    public AbstractPlayerControls(HumanPlayer forPlayer, AbstractWorld inWorld){
        super(forPlayer, inWorld);
    }
    
    public final String meleeString(){
        return "turn to " + mouseString() + "\n use melee";
    }
    public final String attString(int i){
        return "turn to " + mouseString() + "\n use " + i;
    }
    public final String moveString(){
        return "move to " + mouseString();
    }
    
    public void registerControlsTo(Canvas plane){
        plane.registerKey(KeyEvent.VK_Q, true, ()->{
            useMeleeKey();
        });
        plane.registerKey(KeyEvent.VK_1, true, ()->{
            useAttKey(0);
        });
        plane.registerKey(KeyEvent.VK_2, true, ()->{
            useAttKey(1);
        });
        plane.registerKey(KeyEvent.VK_3, true, ()->{
            useAttKey(2);
        });
    }
    public String mouseString(){
        WorldCanvas c = getPlayer().getWorld().getCanvas();
        return String.format("(%d, %d)", c.getMouseX(), c.getMouseY());
    }
    public static int[] decodeMouseString(String s){
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
    public static void decode(HumanPlayer p, String s){
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
        getPlayer().setFollowingMouse(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        getPlayer().setFollowingMouse(false);
    }
    
    @Override
    public void frameEnded(Canvas c) {
        if(getPlayer().getFollowingMouse()){
            move();
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    public abstract void useMeleeKey();
    public abstract void useAttKey(int i);
    public abstract void move();
}
