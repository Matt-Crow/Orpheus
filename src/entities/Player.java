package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import battle.Team;
import customizables.*;
import resources.Op;
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
	private int remHP;
	private int energy;
	private int timeSinceLastHeal;
	private int timeSinceLastEnergy;
	private double damageBacklog;
	private double backLogFilter;
	private Slash slash;
	private int selectedAttack;
	private Attack[] actives;
	private Passive[] passives;
	private ArrayList<Status> statuses;
	
	public Player(String n){
		super(0, 0, 0, 10);
		name = n;
		slash = new Slash();
		actives = new Attack[3];
		passives = new Passive[3];
		statuses = new ArrayList<>();
		selectedAttack = 0;
		players.add(this);
	}
	public String getName(){
		return name;
	}
	public Team getTeam(){
		return team;
	}
	public CharacterClass getCharacterClass(){
		return c;
	}
	public void addFilter(double m){
		backLogFilter *= m;
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
		int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
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
				passives[nameIndex] = new Bracing();
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
			Op.add("Inflicted " + name);
			Op.add("with " + newStatus.getName());
			Op.dp();
			return;
		} else {
			Op.add(name + " is already infliced");
			Op.add("with " + newStatus.getName());
			Op.dp();
			return;
		}
	}
	public void turn(String dir){
		if(turnCooldown <= 0){
			super.turn(dir);
		}
		turnCooldown = 10;
	}
	public int getHP(){
		return remHP;
	}
	public double getHPPerc(){
		return remHP / getStatValue("maxHP") * 100;
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
		damageBacklog += dmg;
	}
	public void logPercentageDamage(double percent){
		damageBacklog += getStatValue("maxHP") * (percent / 100);
	}
	public void depleteBacklog(){
		double damage;
		if(damageBacklog > getStatValue("maxHP") * backLogFilter){
			damage = getStatValue("maxHP") * backLogFilter;
		} else {
			damage = damageBacklog;
		}
		remHP -= damage;
		damageBacklog -= damage;
	}
	
	public void heal(int amount){
		if(remHP == getStatValue("maxHP")){
			return;
		}
		remHP += amount;
		if(remHP > getStatValue("maxHP")){
			remHP = (int) getStatValue("maxHP");
		}
		Op.add("Healed " + amount);
		Op.dp();
	}
	public void healPerc(double percent){
		heal((int) (getStatValue("maxHP") * (percent / 100)));
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
		remHP = (int) getStatValue("maxHP");
		energy = (int) getStatValue("Max energy");
		backLogFilter = 0.01;
		timeSinceLastHeal = 0;
		timeSinceLastEnergy = 0;
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
		Op.dp();
		statuses = newStatuses;
		for(Status s : statuses){
			s.inflictOn(this);
		}
	}
	public void updateBacklog(){
		if(damageBacklog <= 0){
			return;
		}
		Op.add("Before updating backlog for " + name + ":");
		Op.add("*HP remaining: " + remHP);
		Op.add("*Backlog: " + damageBacklog);
		Op.add("*Backlog filter: " + backLogFilter);
		
		depleteBacklog();
		
		Op.add("After updating backlog: ");
		Op.add("*HP remaining: " + remHP);
		Op.add("*Backlog: " + damageBacklog);
		Op.add("*Backlog filter: " + backLogFilter);
		
		Op.dp();
	}
	public void updateHealing(){
		timeSinceLastHeal += 1;
		if(timeSinceLastHeal >= getStatValue("Heal rate")){
			timeSinceLastHeal = 0;
			healPerc(getStatValue("Healing"));
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
		backLogFilter = 0.01;
		slash.update();
		resetTrips();
		for(Attack a : actives){
			a.update();
		}
		updateStatuses();
		for(Passive p : passives){
			p.update();
		}
		tripOnUpdate();
		updateBacklog();
		updateHealing();
		updateEnergy();
	}
	public void draw(Graphics g){
		g.setColor(team.color);
		g.fillOval(getX() - 50, getY() - 50, 100, 100);
		g.setColor(c.getColor());
		g.fillOval(getX() - 40, getY() - 40, 80, 80);
		g.setColor(Color.gray);
	}
	public void drawHUD(Graphics g){
		String strHP = remHP + "";
		g.setColor(Color.red);
		g.fillOval(0, 500, 100, 100);
		g.setColor(Color.black);
		g.drawString(strHP, 5, 550);
		
		String strEn = energy + "";
		g.setColor(Color.yellow);
		g.fillOval(500, 500, 100, 100);
		g.setColor(Color.black);
		g.drawString(strEn, 505, 550);
	}
}