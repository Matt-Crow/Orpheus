package entities;

import java.awt.Color;
import java.awt.Graphics;
import ai.Path;
import ai.PathInfo;
import battle.*;
import statuses.AbstractStatus;
import controllers.Settings;
import world.WorldContent;
import customizables.actives.ElementalActive;
import customizables.characterClass.CharacterStatName;
import graphics.Tile;
import static java.lang.System.out;
import java.util.Arrays;
import util.Direction;
import util.SafeList;

/**
 * The AbstractPlayer class essentially acts as a
 * mobile entity with other, battle related capabilities.
 * 
 * @author Matt Crow
 */
public abstract class AbstractPlayer extends AbstractReactiveEntity{
	private final String name;
	private Color color;
    
    /*
	 * (focusX, focusY) is a point that the entity is trying to reach
	 */
	private int focusX;
	private int focusY;
	private boolean hasFocus;
    
    private Direction knockbackDir;
    private int knockbackMag;
    private int knockbackDur;
    
    private int lastHitById; //the useId of the last projectile that hit this player
    private AbstractPlayer lastHitBy;
	
    private final ElementalActive slash;
	private final DamageBacklog log;
	private final SafeList<AbstractStatus> statuses; //change to hashtable
    //both players and AI need to find paths, given the current controls
    private Path path;
    
    public static final int RADIUS = 50;
    
	
	public AbstractPlayer(String n, int minLifeSpan){
		super();
        setSpeed(Tile.TILE_SIZE * 5 / Settings.FPS);
		name = n;
        color = Color.black;
        
        focusX = 0;
        focusY = 0;
        hasFocus = false;
        
        knockbackDir = null;
        knockbackMag = 0;
        knockbackDur = 0;
        
        slash = (ElementalActive)Settings.getDataSet().getActiveByName("Slash");
		slash.setUser(this);
        log = new DamageBacklog(this, minLifeSpan);
        path = null;
        statuses = new SafeList<>();
        
        lastHitById = -1;
        lastHitBy = null;
        
        setRadius(RADIUS);
	}
	
	public final String getName(){
		return name;
	}
    public final void setColor(Color c){
        color = c;
    }
    
	public DamageBacklog getLog(){
		return log;
	}
    
    //focus related methods
	public final void setFocus(int xCoord, int yCoord){
		focusX = xCoord;
		focusY = yCoord;
		hasFocus = true;
	}
	public final void setFocus(AbstractEntity e){
		setFocus(e.getX(), e.getY());
	}
	public final void turnToFocus(){
		turnTo(focusX, focusY);
	}
	public boolean withinFocus(){
		// returns if has reached focal point
		boolean withinX = Math.abs(getX() - focusX) < getSpeed();
		boolean withinY = Math.abs(getY() - focusY) < getSpeed();
		return withinX && withinY;
	}
    
    public void setPath(int x, int y){
        WorldContent w = getWorld();
        setPath(w.getMap().findPath(getX(), getY(), x, y));
    }
    public void setPath(Path p){
        path = p;
        if(!path.noneLeft()){
            PathInfo pi = path.get();
            setFocus(pi.getEndX(), pi.getEndY());
        }
    }
    public Path getPath(){
        return path;
    }
    
    public final void knockBack(int mag, Direction d, int dur){
        /**
         * @param mag : the total distance this entity will be knocked back
         * @param d : the direction this entity is knocked back
         * @param dur : the number of frames this will be knocked back for
         */
        knockbackMag = mag / dur;
        knockbackDir = d;
        knockbackDur = dur;
    }
    
	public void inflict(AbstractStatus newStat){
		boolean found = false;
		boolean shouldReplace = false;
        
        AbstractStatus[] objStatuses = Arrays
            .stream(statuses.toArray())
            .toArray(size -> new AbstractStatus[size]);
		
        for(AbstractStatus s : objStatuses){
			if(s.getStatusName() == newStat.getStatusName()){
				// already inflicted
				found = true;
				if(s.getIntensityLevel() < newStat.getIntensityLevel()){
					// better level
                    s.terminate();
                    shouldReplace = true;
				} else if(s.getUsesLeft() < newStat.getMaxUses()){
                    s.terminate();
                    shouldReplace = true;
                }
			}
		}
		if(shouldReplace || !found){
			statuses.add(newStat);
            newStat.inflictOn(this);
		}
	}
	
	public void useMeleeAttack(){
		if(slash.canUse()){
			slash.trigger();
		}
	}
	
    /**
     * Notifies this Player that a projectile
     * has hit them.
     * 
     * @param p 
     */
    public final void wasHitBy(Projectile p){
        lastHitById = p.getUseId();
        lastHitBy = p.getUser();
    }
    
	public void logDamage(int dmg){
		log.log(dmg);
	}
	
	public int getLastHitById(){
		return lastHitById;
	}
    
    @Override
	public void init(){
        super.init();
        statuses.clear();
        getActionRegister().reset();
        
		slash.doInit();
		log.init();
		
        path = null;
		lastHitById = -1;
        lastHitBy = null;
        
        hasFocus = false;
        knockbackDir = null;
        knockbackMag = 0;
        knockbackDur = 0;
        
        playerInit();
	}
    
    @Override
    public void updateMovement(){
        if(hasFocus){
			if(withinFocus()){
				hasFocus = false;
				setMoving(false);
			}else{
				turnToFocus();
				setMoving(true);
			}
		}
        if(knockbackDir != null){
            //cannot move if being knocked back
            setX((int) (getX() + knockbackMag * knockbackDir.getXMod()));
            setY((int) (getY() + knockbackMag * knockbackDir.getYMod()));
            knockbackDur--;
            if(knockbackDur == 0){
                knockbackDir = null;
            }
        } else {
            super.updateMovement();
        }   
        clearSpeedFilter();
    }
	
    @Override
	public void update(){
        super.update();
        if(path != null){
            //follow path
            if(path.noneLeft()){
                path = null;
            } else {
                //System.out.println("Refocusing..");
                while(withinFocus() && !path.noneLeft()){
                    path.deque();
                    if(!path.noneLeft()){
                        PathInfo p = path.get();
                        //System.out.println(p);
                        setFocus(p.getEndX(), p.getEndY());
                    }
                }
            }
        }
		slash.doUpdate();
		
		
		getActionRegister().triggerOnUpdate();
		log.update();
        
        playerUpdate();
	}
    
    //can get rid of this later
    public void listStatuses(){
        out.println("STATUSES:");
        statuses.forEach((status)->out.println(status.getName()));
        out.println("End of statues");
    }
    
    @Override
    public void terminate(){
        super.terminate();
        if(lastHitBy != null){
            lastHitBy.getLog().healPerc(5);
        }
        getTeam().notifyTerminate(this);
    }
	
    @Override
	public void draw(Graphics g){
        int w = getWorld()
            .getShell()
            .getCanvas()
            .getWidth();
		int h = getWorld()
            .getShell()
            .getCanvas()
            .getHeight();
		int r = getRadius();
        
        if(path != null){
            path.draw(g);
        }
        
        
		// HP value
		g.setColor(Color.black);
		g.drawString("HP: " + log.getHP(), getX() - w/12, getY() - h/8);
		
		// Update this to sprite later, doesn't scale to prevent hitbox snafoos
		g.setColor(getTeam().getColor());
		g.fillOval(getX() - r, getY() - r, 2 * r, 2 * r);
		g.setColor(color);
		g.fillOval(getX() - (int)(r * 0.8), getY() - (int)(r * 0.8), (int)(r * 1.6), (int)(r * 1.6));
		
		g.setColor(Color.black);
		int y = getY() + h/10;
		for(Object obj : statuses.toArray()){
			AbstractStatus s = (AbstractStatus)obj;
            String iStr = "";
			int i = 0;
			while(i < s.getIntensityLevel()){
				iStr += "I";
				i++;
			}
			g.drawString(s.getName() + " " + iStr + "(" + s.getUsesLeft() + ")", getX() - r, y);
			y += h/30;
		}
	}

    public abstract double getStatValue(CharacterStatName n);
    public abstract void playerInit();
    public abstract void playerUpdate();
}