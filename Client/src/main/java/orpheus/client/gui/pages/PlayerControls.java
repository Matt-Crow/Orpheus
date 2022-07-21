package orpheus.client.gui.pages;

import commands.ControlPressed;
import controls.AbstractControlScheme;
import net.protocols.EndOfFrameListener;
import orpheus.client.gui.pages.play.WorldCanvas;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import start.AbstractOrpheusCommandInterpreter;
import util.CardinalDirection;
import world.World;
import world.entities.AbstractPlayer;
import world.entities.HumanPlayer;

/**
 * CONTROLS:<br>
 * - Q to use melee attack<br>
 * - WASD to move<br>
 * - 1-3 to use active abilities 1-3 respectively<br>
 * 
 * @author Matt Crow
 */
public class PlayerControls extends AbstractControlScheme implements MouseListener, EndOfFrameListener{
    private final AbstractOrpheusCommandInterpreter interpreter;
    
    public PlayerControls(World inWorld, String playerId, AbstractOrpheusCommandInterpreter interpreter) {
        super(inWorld, playerId);
        this.interpreter = interpreter;
    }
    
    private String playerString(){
        return "#" + getPlayerId();
    }
    public final String meleeString(WorldCanvas canvas){
        return playerString() + " turn to " + mouseString(canvas) + "\n use melee";
    }
    public final String attString(int i, WorldCanvas canvas){
        return playerString() + " turn to " + mouseString(canvas) + "\n use " + i;
    }
    public final String moveString(WorldCanvas canvas){
        return playerString() + " move to " + mouseString(canvas);
    }
    public final String directionStartString(CardinalDirection dir){
        return playerString() + " start move direction " + dir.toString();
    }
    public final String directionStopString(CardinalDirection dir){
        return playerString() + " stop move direction " + dir.toString();
    }
    
    public void registerControlsTo(WorldCanvas plane){
        plane.registerKey(KeyEvent.VK_Q, true, ()->{
            useMeleeKey(plane);
        });
        plane.registerKey(KeyEvent.VK_1, true, ()->{
            useAttKey(0, plane);
        });
        plane.registerKey(KeyEvent.VK_2, true, ()->{
            useAttKey(1, plane);
        });
        plane.registerKey(KeyEvent.VK_3, true, ()->{
            useAttKey(2, plane);
        });
        plane.registerKey(KeyEvent.VK_W, true, ()->{
            useDirKey(CardinalDirection.UP);
        });
        plane.registerKey(KeyEvent.VK_A, true, ()->{
            useDirKey(CardinalDirection.LEFT);
        });
        plane.registerKey(KeyEvent.VK_S, true, ()->{
            useDirKey(CardinalDirection.DOWN);
        });
        plane.registerKey(KeyEvent.VK_D, true, ()->{
            useDirKey(CardinalDirection.RIGHT);
        });
        
        plane.registerKey(KeyEvent.VK_W, false, ()->{
            stopUseDirKey(CardinalDirection.UP);
        });
        plane.registerKey(KeyEvent.VK_A, false, ()->{
            stopUseDirKey(CardinalDirection.LEFT);
        });
        plane.registerKey(KeyEvent.VK_S, false, ()->{
            stopUseDirKey(CardinalDirection.DOWN);
        });
        plane.registerKey(KeyEvent.VK_D, false, ()->{
            stopUseDirKey(CardinalDirection.RIGHT);
        });
    }
    
    public String mouseString(WorldCanvas c){
        return String.format("(%d, %d)", c.getMouseX(), c.getMouseY());
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
    public void frameEnded() {
        if(getPlayer() instanceof HumanPlayer && ((HumanPlayer)getPlayer()).getFollowingMouse()){
            //move();
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
    private void useMeleeKey(WorldCanvas canvas){
        consumeCommand(meleeString(canvas));
    }
    private void useAttKey(int i, WorldCanvas canvas){
        consumeCommand(attString(i, canvas));
    }
    private void useDirKey(CardinalDirection d){
        consumeCommand(directionStartString(d));
    }
    private void stopUseDirKey(CardinalDirection d){
        consumeCommand(directionStopString(d));
    }
    
    public static String getPlayerControlScheme(){
        StringBuilder sb = new StringBuilder();
        sb.append("#CONTROLS#\n");
        sb.append("W, A, S, or D keys to move\n");
        sb.append("(Q): use your basic attack\n");
        sb.append("(1-3): use your attacks 1, 2, or 3\n");
        sb.append("(P): pause / resume (singleplayer only)\n");
        sb.append("(Z): zoom in\n");
        sb.append("(X): zoom out\n");
        return sb.toString();
    }

    public void consumeCommand(String command) {
        interpreter.doSendMessage(new ControlPressed(this.getWorld(), command));
    }
}
