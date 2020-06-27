package controls;

import entities.AbstractPlayer;
import entities.AbstractPlayerControlCommand;

/**
 *
 * @author Matt
 */
public class MoveCommand extends AbstractPlayerControlCommand{
    private final int pointX;
    private final int pointY;
    
    public MoveCommand(int x, int y){
        pointX = x;
        pointY = y;
    }
    
    @Override
    public void execute(AbstractPlayer target) {
        target.setFocus(pointX, pointY);
    }    
}
