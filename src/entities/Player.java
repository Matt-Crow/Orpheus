package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import actives.*;
import ai.Path;
import ai.PathInfo;
import battle.*;
import customizables.*;
import passives.*;
import ai.PlayerAI;
import statuses.AbstractStatus;
import statuses.StatusName;
import controllers.Master;
import controllers.World;

public class Player extends Entity{
	private final String name;
	private CharacterClass c;
	private final AbstractActive[] actives;
	private final AbstractPassive[] passives;
	
    private boolean followingMouse;
    
	private DamageBacklog log;
	private EnergyLog energyLog;
	
	private MeleeActive slash;
	private ArrayList<AbstractStatus> statuses;
	
	private PlayerAI playerAI;
	private int lastHitById; //the useId of the last projectile that hit this player
    
    private Path path;
    
    public static final int RADIUS = 50;
    
	
	public Player(String n){
		super();
        setSpeed(Master.UNITSIZE * 5 / Master.FPS);
		name = n;
		actives = new AbstractActive[3];
		passives = new AbstractPassive[3];
        setRadius(RADIUS);
        followingMouse = false;
        path = null;
	}
	
	public String getName(){
		return name;
	}
	
	public AbstractActive[] getActives(){
		return actives;
	}
	
	public DamageBacklog getLog(){
		return log;
	}
	public EnergyLog getEnergyLog(){
		return energyLog;
	}
	public CharacterClass getCharacterClass(){
		return c;
	}
	public PlayerAI getPlayerAI(){
		return playerAI;
	}
    
    public void setFollowingMouse(boolean b){
		followingMouse = b;
	}
	public boolean getFollowingMouse(){
		return followingMouse;
	}
    
    public void setPath(Path p){
        path = p;
        if(!path.noneLeft()){
            PathInfo pi = path.get();
            // System.out.println(pi);
            setFocus(pi.getEndX(), pi.getEndY());
        }
    }
	
	// Build stuff
	public void applyBuild(Build b){
		setClass(b.getClassName());
		setActives(b.getActiveNames());
		setPassives(b.getPassiveNames());
		setSpeed((int) (c.getStatValue(CharacterStatName.SPEED) * (500 / Master.FPS)));
	}
	
	public void setClass(String name){
		c = CharacterClass.getCharacterClassByName(name.toUpperCase());
	}
	public void setActives(String[] names){
		for(int nameIndex = 0; nameIndex < 3; nameIndex ++){
			actives[nameIndex] = AbstractActive.getActiveByName(names[nameIndex]).copy();
			actives[nameIndex].registerTo(this);
		}
	}
	public void setPassives(String[] names){
		for(int nameIndex = 0; nameIndex < 3; nameIndex ++){
			passives[nameIndex] = AbstractPassive.getPassiveByName(names[nameIndex]).copy();
			passives[nameIndex].registerTo(this);
		}
	}
	
	public void inflict(StatusName statusName, int intensity, int duration){
		boolean found = false;
		boolean shouldReplace = false;
		for(AbstractStatus s : statuses){
			if(s.getStatusName() == statusName && !found){
				// already inflicted
				found = true;
				if(s.getIntensityLevel() <= intensity){
					// better level
					if(s.getUsesLeft() < duration){
						s.terminate();
						shouldReplace = true;
					}
				}
			}
		}
		if(shouldReplace || !found){
			statuses.add(AbstractStatus.decode(statusName, intensity, duration));
		}
	}
	
	public double getStatValue(CharacterStatName n){
		return c.getStatValue(n);
	}
	public void useMeleeAttack(){
		if(slash.canUse()){
			slash.use();
		}
	}
	
	public void useAttack(int num){
		if(actives[num].canUse()){
			actives[num].use();
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
		playerAI = new PlayerAI(this);
		path = null;
		if (!(this instanceof TruePlayer)){
			playerAI.setEnabled(true);
		}
		slash = (MeleeActive)AbstractActive.getActiveByName("Slash").copy();
		slash.registerTo(this);
		slash.init();
		c.init();
		log = new DamageBacklog(this);
		energyLog = new EnergyLog(this);
		statuses = new ArrayList<>();
		for(AbstractActive a : actives){
			a.init();
		}
		for(AbstractPassive p : passives){
			p.registerTo(this);
		}
		lastHitById = -1;
	}
	
	public void updateStatuses(){
		ArrayList<AbstractStatus> newStatuses = new ArrayList<>();
		for(AbstractStatus s : statuses){
			if(!s.getShouldTerminate()){
				newStatuses.add(s);
			}
		}
		statuses = newStatuses;
		for(AbstractStatus s : statuses){
			s.inflictOn(this);
		}
	}
	
    @Override
	public void update(){
        if(followingMouse){
            World w = getWorld();
            setPath(w.getMap().findPath(getX(), getY(), w.getCanvas().getMouseX(), w.getCanvas().getMouseY()));
		}
		playerAI.update();
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
		getActionRegister().resetTrips();
		for(AbstractActive a : actives){
			a.update();
		}
		for(AbstractPassive p : passives){
			p.update();
		}
		updateStatuses();
		getActionRegister().tripOnUpdate();
		log.update();
		energyLog.update();
	}
    
    @Override
    public void terminate(){
        super.terminate();
        getTeam().notifyTerminate(this);
    }
	
    @Override
	public void draw(Graphics g){
		int w = Master.CANVASWIDTH;
		int h = Master.CANVASHEIGHT;
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
		g.setColor(c.getColors()[0]);
		g.fillOval(getX() - (int)(r * 0.8), getY() - (int)(r * 0.8), (int)(r * 1.6), (int)(r * 1.6));
		
		g.setColor(Color.black);
		int y = getY() + h/10;
		for(AbstractStatus s : statuses){
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
}