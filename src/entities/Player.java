package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;
import battle.Team;
import customizables.*;

public class Player extends Entity{
	public String name;
	private int turnCooldown;
	private Team team;
	private CharacterClass c;
	
	public Player(String n){
		super();
		name = n;
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
	public void turn(String dir){
		if(turnCooldown <= 0){
			super.turn(dir);
		}
		turnCooldown = 10;
	}
	public int getEnergy(){
		return c.getEnergy();
	}
	public void init(Team t, int x, int y, int dirNum){
		super.init(x, y, dirNum, 10);
		team = t;
		turnCooldown = 0;
	}
	public void update(){
		super.update();
		turnCooldown -= 1;
	}
	public void draw(Graphics g){
		g.setColor(team.color);
		g.fillOval(getX() - 50, getY() - 50, 100, 100);
		g.setColor(c.color);
		g.fillOval(getX() - 40, getY() - 40, 80, 80);
		g.setColor(Color.gray);
	}
}
