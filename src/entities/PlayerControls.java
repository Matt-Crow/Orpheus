package entities;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import windows.DrawingPlane;
import windows.WorldCanvas;

/**
 *
 * @author Matt Crow
 */
public class PlayerControls implements MouseListener{
    Player p;
    
    public PlayerControls(Player forPlayer){
        p = forPlayer;
    }
    
    public void registerControlsTo(DrawingPlane plane){
        plane.registerKey(KeyEvent.VK_Q, true, ()->{
            turnToMouse();
            p.useMeleeAttack();
        });
        plane.registerKey(KeyEvent.VK_1, true, ()->{
            turnToMouse();
            p.useAttack(0);
        });
        plane.registerKey(KeyEvent.VK_2, true, ()->{
            turnToMouse();
            p.useAttack(1);
        });
        plane.registerKey(KeyEvent.VK_3, true, ()->{
            turnToMouse();
            p.useAttack(2);
        });
    }
    
    private void turnToMouse(){
        WorldCanvas c = p.getWorld().getCanvas();
        p.turnTo(c.getMouseX(), c.getMouseY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        //toggle following the mouse
        if(p.getFollowingMouse()){
            p.setFollowingMouse(false);
            p.setMoving(false);
        }else{
            p.setFollowingMouse(true);
            p.setMoving(true);
        }
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
