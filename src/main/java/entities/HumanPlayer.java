package entities;

import controllers.Master;
import customizables.Build;
import customizables.characterClass.CharacterClass;
import customizables.characterClass.CharacterStatName;
import java.util.NoSuchElementException;

/**
 *
 * @author Matt
 */
public class HumanPlayer extends AbstractPlayer{
    private CharacterClass c;
    private boolean followingMouse;
    
    
    public HumanPlayer(String n) {
        super(n);
        c = null;
        followingMouse = false;
    }
    
    // Build stuff
	public void applyBuild(Build b){
		setClass(b.getClassName());
		setActives(b.getActiveNames());
		setPassives(b.getPassiveNames());
		setSpeed((int) (c.getStatValue(CharacterStatName.SPEED) * (500 / Master.FPS)));
    }
    public void setClass(String name){
        try{
            c = CharacterClass.getCharacterClassByName(name.toUpperCase());
            setColor(c.getColors()[0]);
            c.setUser(this);
        } catch(NoSuchElementException ex){
            ex.printStackTrace();
            c = CharacterClass.getCharacterClassByName("Default");
        }
	}
    public CharacterClass getCharacterClass(){
		return c;
	}
    
    @Override
    public double getStatValue(CharacterStatName n){
		return c.getStatValue(n);
	}
    
    public final void setFollowingMouse(boolean b){
        followingMouse = b;
    }
    public final boolean getFollowingMouse(){
        return followingMouse;
    }

    @Override
    public void playerInit() {
        c.init();
    }

    @Override
    public void playerUpdate() {
        
    }
}
