package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;
//import resources.Op;
import resources.Direction;
import battle.Team;
import customizables.*;

public class Player {
	public String name;
	private int x;
	private int y;
	private int dirNum;
	private int turnCooldown;
	private int momentum;
	private boolean moving;
	private Team team;
	private CharacterClass c;
	
	public Player(String n){
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
	public void setCoords(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int[] getVector(){
		return Direction.directions[dirNum];
	}
	public void turn(String dir){
		if(turnCooldown > 0){
			return;
		}
		if(dir == "left"){
			dirNum -= 1;
		} else {
			dirNum += 1;
		}
		if(dirNum < 0){
			dirNum = 7;
		} else if(dirNum > 7){
			dirNum = 0;
		}
		turnCooldown = 10;
	}
	public void setMoving(boolean m){
		moving = m;
	}
	public void init(Team t){
		team = t;
		momentum = 10;
		moving = false;
		dirNum = 0;
		turnCooldown = 0;
	}
	public void update(){
		turnCooldown -= 1;
		if(moving){
			move();
		}
	}
	
	// add collisions
	public void move(){
		x += getVector()[0] * momentum;
		y += getVector()[1] * momentum;
	}
	public void draw(Graphics g){
		g.setColor(team.color);
		g.fillOval(x - 50, y - 50, 100, 100);
		g.setColor(c.color);
		g.fillOval(x - 40, y - 40, 80, 80);
		g.setColor(Color.gray);
	}
}
