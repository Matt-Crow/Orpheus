package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import battle.*;
import customizables.*;
import resources.*;
import attacks.*;
import passives.*;
import ai.AI;
import statuses.Status;
import initializers.Master;

// this is going to be very big
public class Player extends Entity{
	private String name;
	private Team team;
	private CharacterClass c;
	private Attack[] actives;
	private Passive[] passives;
	
	private DamageBacklog log;
	private EnergyLog energyLog;
	
	private Slash slash;
	private int selectedAttack;
	private ArrayList<Status> statuses;
	
	private boolean AI;
	private AI intel;
	
	private boolean shouldTerminate;
	
	public Player(String n){
		super(0, 0, 0, 10);
		name = n;
		slash = new Slash();
		actives = new Attack[3];
		passives = new Passive[3];
		
		AI = !Master.DISABLEALLAI;
	}
	public String getName(){
		return name;
	}
	public Team getTeam(){
		return team;
	}
	public Coordinates getCoords(){
		return new Coordinates(getX(), getY(), this);
	}
	public Attack[] getActives(){
		return actives;
	}
	public int getSelectedAttack(){
		return selectedAttack;
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
	public boolean getHasAI(){
		return AI;
	}
	public AI getAI(){
		return intel;
	}
	public boolean getShouldTerminate(){
		return shouldTerminate;
	}
	
	public void disableAI(){
		AI = false;
	}
	public void terminate(){
		shouldTerminate = true;
	}
	
	// Build stuff
	public void applyBuild(Build b){
		setClass(b.getClassName());
		setActives(b.getActiveNames());
		setPassives(b.getPassiveNames());
	}
	public void setClass(String name){
		switch(name.toLowerCase()){
			case "fire":
				c = new Fire();
				return;
			case "earth":
				c = new Earth();
				return;
			case "water":
				c = new Water();
				return;
			case "air":
				c = new Air();
				return;
		}
		String[] classes = {"fire", "earth", "water", "air"};
		int randomNum = Random.choose(0, 4);
		setClass(classes[randomNum]);
	}
	public void setActives(String[] names){
		for(int nameIndex = 0; nameIndex < 3; nameIndex ++){
			boolean found = false;
			for(Attack a : c.getAttackOption()){
				if(a.getName() == names[nameIndex]){
					actives[nameIndex] = a;
					found = true;
					break;
				}
			}
			if(!found){
				actives[nameIndex] = new Slash();
				Op.add("The active by the name of " + names[nameIndex]);
				Op.add("is not found for the characterClass " + c.getName());
				Op.dp();
			}
		}
	}
	public void setPassives(String[] names){
		for(int nameIndex = 0; nameIndex < 3; nameIndex ++){
			boolean found = false;
			for(Passive p : c.getPassiveOptions()){
				if(p.getName() == names[nameIndex]){
					passives[nameIndex] = p;
					found = true;
					break;
				}
			}
			if(!found){
				passives[nameIndex] = new Passive("UNDEFINED", "ERROR");
				Op.add("The passive by the name of " + names[nameIndex]);
				Op.add("is not found for the characterClass " + c.getName());
				Op.dp();
			}
		}
	}
	
	public void inflict(Status newStatus){
		Status oldStatus = new Status("Placeholder", -1, -1);
		int oldLevel = 0;
		
		for(Status s : statuses){
			if(s.getName() == newStatus.getName()){
				oldStatus = s;
				oldLevel = s.getIntensityLevel();
				break;
			}
		}		
				
		if(oldLevel < newStatus.getIntensityLevel()){
			oldStatus.terminate();
			statuses.add(newStatus);
			newStatus.reset();
			return;
		} else {
			oldStatus.reset();
			return;
		}
	}
	
	public void turnToward(Direction d){
		int cDirNum = getDir().getDegrees();
		int dDirNum = d.getDegrees();
		boolean shouldLeft = true;
		
		if(cDirNum < dDirNum){
			shouldLeft = true;
		} else if(cDirNum > dDirNum){
			shouldLeft = false;
		} else {
			// already facing the correct way
			return;
		}
		
		double differenceBetween;
		if(cDirNum > dDirNum){
			differenceBetween = cDirNum - dDirNum;
		} else {
			differenceBetween = dDirNum - cDirNum;
		}
		
		if(differenceBetween > 180){
			shouldLeft = !shouldLeft;
		}
		
		if(shouldLeft){
			setWillTurn("left");
		} else {
			setWillTurn("right");
		}
	}
	
	public double getStatValue(String n){
		return c.getStatValue(n);
	}
	public void useMeleeAttack(){
		if(slash.canUse(this)){
			slash.use(this);
		}
	}
	public void changeSelectedAttack(int index){
		selectedAttack = index;
	}
	public void useSelectedAttack(){
		if(actives[selectedAttack].canUse(this)){
			actives[selectedAttack].use(this);
		}
	}
	
	public void logDamage(int dmg){
		log.log(dmg);
	}
	
	public void init(Team t, int x, int y, Direction d){
		if(AI){
			intel = new AI(this);
			intel.setToWander();
		}
		super.setCoords(x, y);
		super.setDir(d);
		team = t;
		slash.init();
		c.calcStats();
		log = new DamageBacklog(this);
		energyLog = new EnergyLog(this);
		statuses = new ArrayList<>();
		selectedAttack = 0;
		for(Attack a : actives){
			a.init();
		}
		for(Passive p : passives){
			p.registerTo(this);
		}
		shouldTerminate = false;
	}
	
	public void updateStatuses(){
		ArrayList<Status> newStatuses = new ArrayList<>();
		for(Status s : statuses){
			if(s.getShouldTerminate()){
				
			}else{
				newStatuses.add(s);
			}
		}
		statuses = newStatuses;
		for(Status s : statuses){
			s.inflictOn(this);
		}
	}
	
	public void update(){
		super.update();
		slash.update();
		resetTrips();
		for(Attack a : actives){
			a.update();
		}
		for(Passive p : passives){
			p.update();
		}
		updateStatuses();
		tripOnUpdate();
		log.update();
		energyLog.update();
		
		if(AI){
			intel.update();
		}
	}
	
	public void draw(Graphics g){
		int w = Master.CANVASWIDTH;
		int h = Master.CANVASHEIGHT;
		
		// HP value
		g.setColor(Color.black);
		g.drawString("HP: " + log.getHP(), getX() - w/12, getY() - h/8);
		
		// Update this to sprite later, doesn't scale to prevent hitbox snafoos
		g.setColor(team.getColor());
		g.fillOval(getX() - 50, getY() - 50, 100, 100);
		g.setColor(c.getColor());
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