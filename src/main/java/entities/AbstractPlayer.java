package entities;

import customizables.actives.AbstractActive;
import customizables.actives.MeleeActive;
import java.awt.Color;
import java.awt.Graphics;
import ai.Path;
import ai.PathInfo;
import battle.*;
import statuses.AbstractStatus;
import controllers.Master;
import controllers.World;
import customizables.characterClass.CharacterStatName;
import static java.lang.System.out;
import java.util.Arrays;
import util.SafeList;

/**
 * The AbstractPlayer class essentially acts as a
 * mobile entity with other, battle related capabilities.
 * 
 * @author Matt Crow
 */
public abstract class AbstractPlayer extends Entity{
	private final String name;
	private Color color;
	
	private final DamageBacklog log;
	
	private final MeleeActive slash;
    
	private final SafeList<AbstractStatus> statuses; //change to hashtable
	private int lastHitById; //the useId of the last projectile that hit this player
    //both players and AI need to find paths, given the current controls
    private Path path;
    
    public static final int RADIUS = 50;
    
	
	public AbstractPlayer(String n, int minLifeSpan){
		super();
        setSpeed(Master.UNITSIZE * 5 / Master.FPS);
		name = n;
        color = Color.black;
        
        slash = (MeleeActive)AbstractActive.getActiveByName("Slash");
		slash.setUser(this);
        log = new DamageBacklog(this, minLifeSpan);
		
		setRadius(RADIUS);
        path = null;
        statuses = new SafeList<>();
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
    
    public void setPath(int x, int y){
        World w = getWorld();
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
			slash.use();
		}
	}
	
	public void logDamage(int dmg){
		log.log(dmg);
	}
	public void setLastHitById(int id){
		lastHitById = id;
	}
	public int getLastHitById(){
		return lastHitById;
	}
    
    @Override
	public void init(){
        statuses.clear();
        getActionRegister().reset();
        
		slash.init();
		log.init();
		
        path = null;
		lastHitById = -1;
        
        playerInit();
	}
	
    @Override
	public void update(){
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
		slash.update();
		
		
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
        getTeam().notifyTerminate(this);
    }
	
    @Override
	public void draw(Graphics g){
        int w = getWorld().getCanvas().getWidth();
		int h = getWorld().getCanvas().getHeight();
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