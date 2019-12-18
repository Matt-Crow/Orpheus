package entities;

import controllers.Master;
import customizables.Build;
import customizables.actives.AbstractActive;
import customizables.characterClass.CharacterClass;
import customizables.characterClass.CharacterStatName;
import java.util.NoSuchElementException;

/**
 *
 * @author Matt
 */
public class HumanPlayer extends AbstractPlayer{
    private CharacterClass c;
    private final AbstractActive[] actives;
	
    private boolean followingMouse;
    
    
    public HumanPlayer(String n) {
        super(n);
        c = null;
        actives = new AbstractActive[3];
		
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
    
    public void setActives(String[] names){
		for(int nameIndex = 0; nameIndex < 3; nameIndex ++){
            try{
                actives[nameIndex] = AbstractActive.getActiveByName(names[nameIndex]);
            } catch(NoSuchElementException ex){
                ex.printStackTrace();
                actives[nameIndex] = AbstractActive.getActiveByName("Default");
            }
			actives[nameIndex].setUser(this);
		}
	}
    public AbstractActive[] getActives(){
		return actives;
    }
    
    
    @Override
    public double getStatValue(CharacterStatName n){
		return c.getStatValue(n);
	}
    
    public void useAttack(int num){
		if(actives[num].canUse()){
			actives[num].use();
		}
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
        for(AbstractActive a : actives){
			a.init();
		}
    }

    @Override
    public void playerUpdate() {
        for(AbstractActive a : actives){
			a.update();
		}
    }
}
