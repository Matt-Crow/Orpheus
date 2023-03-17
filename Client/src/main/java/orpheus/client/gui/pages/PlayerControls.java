package orpheus.client.gui.pages;

import orpheus.client.gui.pages.play.WorldCanvas;
import orpheus.core.commands.StartMoving;
import orpheus.core.commands.StopMoving;
import orpheus.core.commands.TurnTo;
import orpheus.core.commands.UseActive;
import orpheus.core.commands.UseCantrip;
import orpheus.core.commands.executor.Executor;

import java.awt.event.KeyEvent;
import java.util.UUID;

import util.CardinalDirection;

/**
 * CONTROLS:<br>
 * - Q to use melee attack<br>
 * - WASD to move<br>
 * - 1-3 to use active abilities 1-3 respectively<br>
 * 
 * @author Matt Crow
 */
public class PlayerControls {

    private final UUID playerId;
    private final Executor commandExecutor;
    
    public PlayerControls(UUID playerId, Executor commandExecutor) {
        this.playerId = playerId;
        this.commandExecutor = commandExecutor;
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

    public UUID getPlayerId() {
        return playerId;
    }
    
    private void useMeleeKey(WorldCanvas canvas){
        commandExecutor.execute(new UseCantrip(playerId, turnTo(canvas)));
    }
    
    private void useAttKey(int i, WorldCanvas canvas){
        commandExecutor.execute(new UseActive(playerId, turnTo(canvas), i));
    }

    private TurnTo turnTo(WorldCanvas canvas) {
        return new TurnTo(playerId, canvas.getMouseX(), canvas.getMouseY());
    }
    
    private void useDirKey(CardinalDirection d){
        commandExecutor.execute(new StartMoving(playerId, d));
    }
    
    private void stopUseDirKey(CardinalDirection d){
        commandExecutor.execute(new StopMoving(playerId, d));
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
}
