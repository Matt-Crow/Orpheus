package controls.userControls;

import controls.AbstractControlScheme;
import world.entities.AbstractPlayer;
import world.entities.HumanPlayer;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import gui.pages.Canvas;
import gui.pages.EndOfFrameListener;
import gui.pages.worldPlay.WorldCanvas;
import world.AbstractWorldShell;

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
public abstract class AbstractPlayerControls extends AbstractControlScheme implements MouseListener, EndOfFrameListener{
    public AbstractPlayerControls(AbstractWorldShell inWorld, String playerId){
        super(inWorld, playerId);
    }
    
    public final String meleeString(){
        return "#" + getPlayerId() + " turn to " + mouseString() + "\n use melee";
    }
    public final String attString(int i){
        return "#" + getPlayerId() + " turn to " + mouseString() + "\n use " + i;
    }
    public final String moveString(){
        return "#" + getPlayerId() + " move to " + mouseString();
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
        WorldCanvas c = getPlayer().getWorld().getShell().getCanvas();
        return String.format("(%d, %d)", c.getMouseX(), c.getMouseY());
    }
    public static int[] decodeMouseString(String s){
        String coords = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
        String[] split = coords.split(",");
        int x = Integer.parseInt(split[0].trim());
        int y = Integer.parseInt(split[1].trim());
        return new int[]{x, y};
    }
    
    public static void decode(AbstractWorldShell world, String s){
        int[] coords;
        HumanPlayer p = null;
        if(s.contains("#")){
            // contains player id
            int startIndex = s.indexOf("#") + 1;
            int endIndex = s.indexOf(" ", startIndex);
            String playerId = s.substring(startIndex, endIndex);
            p = (HumanPlayer)world.getPlayerTeam().getMemberById(playerId);
        } else {
            throw new UnsupportedOperationException("cannot decode string with no player id");
        }
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
        AbstractPlayer player = getPlayer();
        if(player instanceof HumanPlayer){
            ((HumanPlayer)getPlayer()).setFollowingMouse(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        AbstractPlayer player = getPlayer();
        if(player instanceof HumanPlayer){
            ((HumanPlayer)getPlayer()).setFollowingMouse(false);
        }
    }
    
    @Override
    public void frameEnded(Canvas c) {
        if(getPlayer() instanceof HumanPlayer && ((HumanPlayer)getPlayer()).getFollowingMouse()){
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
