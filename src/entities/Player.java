package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import actives.*;
import battle.*;
import customizables.*;
import resources.*;
import passives.*;
import ai.PlayerAI;
import statuses.Status;
import statuses.StatusName;
import initializers.Master;

// this is going to be very big
public class Player extends Entity{
	private String name;
	private CharacterClass c;
	private AbstractActive[] actives;
	private AbstractPassive[] passives;
	
	private DamageBacklog log;
	private EnergyLog energyLog;
	
	private MeleeActive slash;
	private ArrayList<Status> statuses;
	
	private PlayerAI playerAI;
	private int lastHitById; //the useId of the last projectile that hit this player
	
	public Player(String n){
		super(500 / Master.FPS);
		name = n;
		actives = new AbstractActive[3];
		passives = new AbstractPassive[3];
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
		for(Status s : statuses){
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
			statuses.add(Status.decode(statusName, intensity, duration));
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
	
	public void init(Team t, int x, int y, Direction d){
		super.init(x, y, d.getDegrees());
		playerAI = new PlayerAI(this);
		
		if (!(this instanceof TruePlayer) && !Master.DISABLEALLAI){
			playerAI.enable();
		}
		setTeam(t);
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
		ArrayList<Status> newStatuses = new ArrayList<>();
		for(Status s : statuses){
			if(!s.getShouldTerminate()){
				newStatuses.add(s);
			}
		}
		statuses = newStatuses;
		for(Status s : statuses){
			s.inflictOn(this);
		}
	}
	
	public void update(){
		playerAI.update();
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
	
	public void draw(Graphics g){
		int w = Master.CANVASWIDTH;
		int h = Master.CANVASHEIGHT;
		
		for(AbstractActive a : actives){
			a.drawProjectiles(g);
		}
		
		// HP value
		g.setColor(Color.black);
		g.drawString("HP: " + log.getHP(), getX() - w/12, getY() - h/8);
		
		// Update this to sprite later, doesn't scale to prevent hitbox snafoos
		g.setColor(getTeam().getColor());
		g.fillOval(getX() - 50, getY() - 50, 100, 100);
		g.setColor(c.getColors()[0]);
		g.fillOval(getX() - 40, getY() - 40, 80, 80);
		
		g.setColor(Color.black);
		int y = getY() + h/10;
		for(Status s : statuses){
			String iStr = "";
			int i = 0;
			while(i < s.getIntensityLevel()){
				iStr += "I";
				i++;
			}
			g.drawString(s.getName() + " " + iStr + "(" + s.getUsesLeft() + ")", getX() - 50, y);
			y += h/30;
		}
	}
}