package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import battle.Team;
import battle.DamageBacklog;
import battle.EnergyLog;
import customizables.*;
import initializers.Master;
import resources.Op;
import resources.Random;
import resources.Coordinates;
import resources.Direction;
import attacks.*;
import passives.*;
import statuses.Status;
import ai.AI;

// this is going to be very big
public class Player extends Entity{
	private static ArrayList<Player> players = new ArrayList<>();
	private String name;
	private int turnCooldown;
	private Team team;
	private CharacterClass c;
	
	private DamageBacklog log;
	private EnergyLog energyLog;
	
	private Slash slash;
	private int selectedAttack;
	private Attack[] actives;
	private Passive[] passives;
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
		
		players.add(this);
		AI = true;
		if(Master.DISABLEALLAI){
			AI = false;
		}
	}
	public String getName(){
		return name;
	}
	public Team getTeam(){
		return team;
	}
	public Coordinates getCoords(){
		return new Coordinates(getX(), getY());
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
	public void turn(String dir){
		if(turnCooldown <= 0){
			super.turn(dir);
			turnCooldown = 10;
		}
	}
	public void decreaseTurnCooldown(int amount){
		turnCooldown -= amount;
	}
	public void turnToward(String d){
		int cDirNum = getDirNum();
		int dDirNum = Direction.getIndexOf(d);
		boolean shouldLeft = true;
		
		if(cDirNum > dDirNum){
			shouldLeft = true;
		} else if(cDirNum < dDirNum){
			shouldLeft = false;
		} else {
			// already facing the correct way
			return;
		}
		
		int differenceBetween;
		if(cDirNum > dDirNum){
			differenceBetween = cDirNum - dDirNum;
		} else if(cDirNum < dDirNum){
			differenceBetween = dDirNum - cDirNum;
		} else {
			// already facing the correct way
			return;
		}
		
		if(differenceBetween > 4){
			shouldLeft = !shouldLeft;
		}
		
		if(shouldLeft){
			turn("left");
		} else {
			turn("right");
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
	
	//Backlog stuff
	public void logDamage(int dmg){
		log.log(dmg);
	}
	public void logPercentageDamage(double percent){
		log.log( (int) (getStatValue("maxHP") * (percent / 100)));
	}
	public void addFilter(double m){
		log.applyFilter(m);
	}
	
	public void init(Team t, int x, int y, int dirNum){
		super.setCoords(x, y);
		super.setDirNum(dirNum);
		team = t;
		turnCooldown = 0;
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
		intel = new AI(this);
		intel.setToWander();
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
		turnCooldown -= 1;
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
		g.setColor(team.getColor());
		g.drawString("HP: " + log.getHP(), getX() - 50, getY() - 75);
		g.fillOval(getX() - 50, getY() - 50, 100, 100);
		g.setColor(c.getColor());
		g.fillOval(getX() - 40, getY() - 40, 80, 80);
		g.setColor(Color.black);
		int y = getY() + 55;
		for(Status s : statuses){
			String iStr = "";
			int i = 0;
			while(i < s.getIntensityLevel()){
				iStr += "I";
				i++;
			}
			g.drawString(s.getName() + " " + iStr + "(" + s.getUsesLeft() + ")", getX() - 50, y);
			y += 20;
		}
	}
	public void drawHUD(Graphics g){
		String strHP = log.getHP() + "";
		g.setColor(Color.red);
		g.fillOval(0, 500, 100, 100);
		g.setColor(Color.black);
		g.drawString(strHP, 5, 550);
		
		String strEn = energyLog.getEnergy() + "";
		g.setColor(Color.yellow);
		g.fillOval(500, 500, 100, 100);
		g.setColor(Color.black);
		g.drawString(strEn, 505, 550);
		
		int i = 100;
		for(Attack a : actives){
			if(a == actives[selectedAttack]){
				g.setColor(Color.yellow);
				g.fillRect(i, 500, 133, 50);
			}
			a.drawStatusPane(g, i, 550);
			i += 133;
		}
	}
}