package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import battle.Team;
import customizables.*;
import resources.Op;
import attacks.*;

public class Player extends Entity{
	private static ArrayList<Player> players = new ArrayList<>();
	private String name;
	private int turnCooldown;
	private Team team;
	private CharacterClass c;
	private Slash slash;
	private ArrayList<Attack> actives;
	private int selectedAttack;
	
	public Player(String n){
		super(0, 0, 0, 10);
		name = n;
		slash = new Slash();
		actives = new ArrayList<>();
		actives.add(new Slash());
		actives.add(new Slash());
		actives.add(new Slash());
		selectedAttack = 0;
		players.add(this);
	}
	public String getName(){
		return name;
	}
	public Team getTeam(){
		return team;
	}
	public String getClassName(){
		return c.name;
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
		actives = new ArrayList<>();
		
		for(int num = 0; num < 3; num++){
			actives.add(Attack.getAttackByName(names[num]));
			Op.add(actives.get(num).getName());
		}
		Op.dp();
	}
	public void turn(String dir){
		if(turnCooldown <= 0){
			super.turn(dir);
		}
		turnCooldown = 10;
	}
	public int getEnergy(){
		return c.getEnergy();
	}
	public void useMeleeAttack(){
		if(slash.canUse(this)){
			slash.use(this);
		}
	}
	public void useSelectedAttack(){
		if(actives.get(selectedAttack).canUse(this)){
			actives.get(selectedAttack).use(this);
		}
	}
	public void init(Team t, int x, int y, int dirNum){
		super.setCoords(x, y);
		super.setDirNum(dirNum);
		team = t;
		turnCooldown = 0;
		slash.init();
		for(Attack a : actives){
			a.init();
		}
		c.initForBattle();
		
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
		g.setColor(c.color);
		g.fillOval(getX() - 40, getY() - 40, 80, 80);
		g.setColor(Color.gray);
	}
}
