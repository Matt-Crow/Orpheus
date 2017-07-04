package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import battle.AttackInstance;
import battle.Team;
import customizables.*;
import resources.Op;
import attacks.*;
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
	private double damageBacklog;
	private Slash slash;
	private int selectedAttack;
	private Attack[] actives;
	private ArrayList<Status> statuses;
	
	public Player(String n){
		super(0, 0, 0, 10);
		name = n;
		slash = new Slash();
		actives = new Attack[3];
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
	public Attack getActive(int index){
		return actives[index];
	}
	public CharacterClass getCharacterClass(){
		return c;
	}
	public void applyBuild(Build b){
		setClass(b.getClassName());
		setActives(b.getActiveNames());
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
	public void inflict(Status s){
		statuses.add(s);
		s.inflictOn(this);
		Op.add("Inflicted " + name);
		Op.add("with " + s.getName());
		Op.dp();
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
		if(getActive(selectedAttack).canUse(this)){
			getActive(selectedAttack).use(this);
		}
	}
	public void logDamage(AttackInstance attack){
		damageBacklog += attack.calcDamage();
	}
	public void depleteBacklog(){
		double damage;
		if(damageBacklog > getStatValue("maxHP")){
			damage = getStatValue("maxHP") / 100;
		} else {
			damage = damageBacklog;
		}
		remHP -= damage;
		damageBacklog -= damage;
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
		for(Attack a : actives){
			a.init();
		}
	}
	public void update(){
		super.update();
		turnCooldown -= 1;
		slash.update();
		for(Attack a : actives){
			a.update();
		}
	}
	public void draw(Graphics g){
		g.setColor(team.color);
		g.fillOval(getX() - 50, getY() - 50, 100, 100);
		g.setColor(c.getColor());
		g.fillOval(getX() - 40, getY() - 40, 80, 80);
		g.setColor(Color.gray);
	}
}