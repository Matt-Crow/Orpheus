package world.entities;

import util.Settings;
import world.build.Build;
import world.build.DataSet;
import world.build.actives.AbstractActive;
import world.build.characterClass.CharacterClass;
import world.build.characterClass.CharacterStatName;
import world.build.passives.AbstractPassive;
import gui.graphics.CustomColors;
import java.awt.Color;
import java.awt.Graphics;
import java.util.NoSuchElementException;
import gui.pages.worldPlay.WorldCanvas;
import java.util.HashMap;
import java.util.Map.Entry;
import util.CardinalDirection;
import util.Direction;
import world.WorldContent;

/**
 *
 * @author Matt
 */
public class HumanPlayer extends AbstractPlayer{
    private CharacterClass c;
    private final AbstractActive[] actives;
	private final AbstractPassive[] passives;
    private boolean followingMouse;
    private final HashMap<CardinalDirection, Boolean> movingInCardinalDir; // used for key controls 
    
    public static final int MIN_LIFE_SPAN = 10;
    
    public HumanPlayer(WorldContent inWorld, String n) {
        super(inWorld, n, MIN_LIFE_SPAN);
        c = null;
        actives = new AbstractActive[3];
		passives = new AbstractPassive[3];
        setClass("Default");
        movingInCardinalDir = new HashMap<>();
        clearMovement();
    }
    
    private void clearMovement(){
        for(CardinalDirection dir : CardinalDirection.values()){
            movingInCardinalDir.put(dir, false);
        }
        followingMouse = false;
    }
    
    // Build stuff
	public void applyBuild(Build b){
		setClass(b.getClassName());
		setActives(b.getActiveNames());
		setPassives(b.getPassiveNames());
		setMaxSpeed((int) (c.getSpeed() * (500.0 / Settings.FPS)));
    }
    public void setClass(String name){
        DataSet ds = Settings.getDataSet();
        try{
            c = ds.getCharacterClassByName(name.toUpperCase());
        } catch(NoSuchElementException ex){
            ex.printStackTrace();
            c = ds.getDefaultCharacterClass();
        }
        setColor(c.getColors()[0]);
        c.setUser(this);
	}
    public CharacterClass getCharacterClass(){
		return c;
	}
    
    public void setActives(String[] names){
		DataSet ds = Settings.getDataSet();
        for(int nameIndex = 0; nameIndex < 3; nameIndex ++){
            try{
                actives[nameIndex] = ds.getActiveByName(names[nameIndex]);
            } catch(NoSuchElementException ex){
                ex.printStackTrace();
                actives[nameIndex] = ds.getDefaultActive();
            }
			actives[nameIndex].setUser(this);
		}
	}
    public AbstractActive[] getActives(){
		return actives;
    }
    
    public void setPassives(String[] names){
        DataSet ds = Settings.getDataSet();
		for(int nameIndex = 0; nameIndex < 3; nameIndex ++){
            try{
                passives[nameIndex] = ds.getPassiveByName(names[nameIndex]);
            } catch(NoSuchElementException ex){
                ex.printStackTrace();
                passives[nameIndex] = ds.getDefaultPassive();
            }
            passives[nameIndex].setUser(this);
		}
	}
    
    public void useAttack(int num){
		if(actives[num].canUse()){
			actives[num].trigger();
		}
	}
    
    public final void setFollowingMouse(boolean b){
        followingMouse = b;
    }
    public final boolean getFollowingMouse(){
        return followingMouse;
    }
    
    public final void setMovingInDir(CardinalDirection dir, boolean isMoving){
        movingInCardinalDir.put(dir, isMoving);
    }
    
    public void moveToMouse(){
        setPath(getWorld().getShell().getCanvas().getMouseX(), getWorld().getShell().getCanvas().getMouseY());
    }

    @Override
    public void playerInit() {
        c.init();
        for(AbstractActive a : actives){
			a.init();
		}
        for(AbstractPassive p : passives){
			p.init();
		}
        clearMovement();
    }

    @Override
    public void playerUpdate() {
        for(AbstractActive a : actives){
			a.update();
		}
        for(AbstractPassive p : passives){
			p.update();
		}
        
        if(this.getPath() == null){ //prevent double movement
            int dx = 0;
            int dy = 0;
            for(Entry<CardinalDirection, Boolean> dir : movingInCardinalDir.entrySet()){
                if(dir.getValue()){
                    dx += dir.getKey().getXMod();
                    dy += dir.getKey().getYMod();
                }
            }
            
            if(dx != 0 || dy != 0){
                Direction newFacing = Direction.getDegreeByLengths(0, 0, dx, dy);
                setFacing(newFacing.getDegrees());
                setIsMoving(true);
            } else {
                setIsMoving(false);
            }
        }
    }
    
    public void drawHUD(Graphics g, WorldCanvas wc){
        int w = wc.getWidth();
		int h = wc.getHeight();
		
		// compass
		int compassX = w / 10 * 9; // center points
		int compassY = h / 10 * 3;
		int compassDiameter = w / 10;
		
		g.setColor(CustomColors.darkGrey);
		g.fillOval(compassX - compassDiameter, compassY - compassDiameter, compassDiameter * 2, compassDiameter * 2); // draws from upper-left corner, not center
		g.setColor(CustomColors.red);
		g.drawLine(compassX, compassY, (int)(compassX + getFacing().getXMod() * compassDiameter), (int)(compassY + getFacing().getYMod() * compassDiameter));
		
		
		int guiY = (int)(h * 0.9);
		int sw = w / 5;
		int sh = h / 10;
		
		// HP
		String strHP = getLog().getHP() + "";
		g.setColor(Color.red);
		g.fillRect(0, guiY, sw, sh);
		g.setColor(Color.black);
		g.drawString("HP: " + strHP, (int)(w * 0.1), (int) (h * 0.93));
		
		// Actives
		int i = sw;
		for(AbstractActive a : getActives()){
			a.drawStatusPane(g, i, (int)(h * 0.9), sw, sh);
			i += sw;
		}
	}

    @Override
    public double getStatValue(CharacterStatName n) {
        double ret = 0.0;
        switch(n){
            case HP:
                ret = c.getMaxHP();
                break;
            case DMG:
                ret = c.getOffMult();
                break;
            case REDUCTION:
                ret = c.getDefMult();
                break;
            case SPEED:
                ret = c.getSpeed();
                break;
        }
        return ret;
    }
}
