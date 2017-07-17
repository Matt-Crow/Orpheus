package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import battle.Team;
import battle.DamageBacklog;
import customizables.*;
import resources.Op;
import resources.Random;
import resources.Coordinates;
import attacks.*;
import passives.*;
import statuses.Status;

// this is going to be very big
public class Player extends Entity{
	private static ArrayList<Player> players = new ArrayList<>();
	private String name;
	private int turnCooldown;
	private Team team;
	private CharacterClass c;
	
	private int energy;
	
	private int timeSinceLastEnergy;
	
	private DamageBacklog log;
	
	private Slash slash;
	private int selectedAttack;
	private Attack[] actives;
	private Passive[] passives;
	private ArrayList<Status> statuses;
	
	private boolean AI;
	
	public Player(String n){
		super(0, 0, 0, 10);
		name = n;
		slash = new Slash();
		actives = new Attack[3];
		passives = new Passive[3];
		statuses = new ArrayList<>();
		selectedAttack = 0;
		players.add(this);
		
		AI = true;
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
	public CharacterClass getCharacterClass(){
		return c;
	}
	public void disableAI(){
		AI = false;
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
		}
		turnCooldown = 10;
	}
	public int getEnergy(){
		return energy;
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
	public void logPercentageDamage(double percent){
		log.log( (int) (getStatValue("maxHP") * (percent / 100)));
	}
	public void addFilter(double m){
		log.applyFilter(m);
	}
	
	public void gainEnergy(int amount){
		if(energy == getStatValue("Max energy")){
			return;
		}
		energy += amount;
		if(energy > getStatValue("Max energy")){
			energy = (int) getStatValue("Max energy");
		}
	}
	public void loseEnergy(int amount){
		energy -= amount;
	}
	public void init(Team t, int x, int y, int dirNum){
		super.setCoords(x, y);
		super.setDirNum(dirNum);
		team = t;
		turnCooldown = 0;
		slash.init();
		c.calcStats();
		energy = (int) getStatValue("Max energy");
		timeSinceLastEnergy = 0;
		log = new DamageBacklog(this);
		for(Attack a : actives){
			a.init();
		}
		for(Passive p : passives){
			p.registerTo(this);
		}
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
	public void updateEnergy(){
		timeSinceLastEnergy += 1;
		if(timeSinceLastEnergy >= getStatValue("ER")){
			timeSinceLastEnergy = 0;
			gainEnergy((int) getStatValue("EPR"));
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
		updateEnergy();
		
		if(AI){
			runAICheck();
		}
	}
	public void draw(Graphics g){
		g.setColor(team.color);
		g.drawString("HP: " + log.getHP(), getX() - 50, getY() - 75);
		g.fillOval(getX() - 50, getY() - 50, 100, 100);
		g.setColor(c.getColor());
		g.fillOval(getX() - 40, getY() - 40, 80, 80);
		g.setColor(Color.black);
		int i = getY() + 55;
		for(Status s : statuses){
			g.drawString(s.getName() + "(" + s.getUsesLeft() + ")", getX() - 50, i);
			i += 20;
		}
	}
	public void drawHUD(Graphics g){
		String strHP = log.getHP() + "";
		g.setColor(Color.red);
		g.fillOval(0, 500, 100, 100);
		g.setColor(Color.black);
		g.drawString(strHP, 5, 550);
		
		String strEn = energy + "";
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
	// AI stuff
	public void findClosestPlayer(){
		Player closest;
		double dist = 999999;
		// work here later
	}
	public boolean checkIfPlayerInRange(){
		for(Coordinates c : getTeam().getEnemy().getAllCoords()){
			if(c.distanceBetween(getCoords()) <= 100){
				return true;
			}
		}
		return false;
	}
	public void runAICheck(){
		setMoving(!checkIfPlayerInRange());
	}
}