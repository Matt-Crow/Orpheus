package entities;

/**
 *
 * @author Matt
 */
public class HumanPlayer extends AbstractPlayer{
    private boolean followingMouse;
    
    public HumanPlayer(String n) {
        super(n);
        followingMouse = false;
    }
    
    public final void setFollowingMouse(boolean b){
        followingMouse = b;
    }
    public final boolean getFollowingMouse(){
        return followingMouse;
    }

    @Override
    public void playerInit() {
        
    }

    @Override
    public void playerUpdate() {
        
    }
}
