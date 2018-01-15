package battle;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;

import entities.Entity;
import entities.Player;
import entities.Projectile;
import resources.Coordinates;
import resources.Random;
import resources.Direction;
import customizables.Build;

public class Team {
	private String name;
	private Color color;
	private Team enemyTeam;
	private boolean defeated;
	
	// use as head for linked list
	private Player coach;
	
	public Team(String n, Color c){
		name = n;
		color = c;
		coach = new Player("Don't Update");
	}
	public String getName(){
		return name;
	}
	public Color getColor(){
		return color;
	}
	public ArrayList<Coordinates> getAllCoords(){
		ArrayList<Coordinates> ret = new ArrayList<>();
		Entity current = coach;
		while(current.getHasChild()){
			current = current.getChild(); // skip head
			ret.add(current.getCoords());
		}
		return ret;
	}
	
	public void addMember(Player m){
		coach.insertChild(m);
	}
	public static Team constructRandomTeam(String name, Color color, int size){
		Team t = new Team(name, color);
		for(int teamSize = 0; teamSize < size; teamSize++){
			Player p = new Player(name + " member #" + (teamSize + 1));
			p.applyBuild(Build.getAllBuilds().get(Random.choose(0, Build.getAllBuilds().size() - 1)));
			
			t.addMember(p);
		}
		return t;
	}
	public void init(int y, int spacing, int facing){
		int x = spacing;
		Player current = coach;
		while(current.getHasChild()){
			current = (Player) current.getChild();
			current.init(this, x, y, new Direction(facing));
			x += spacing;
		}
		defeated = false;
	}
	public void setEnemy(Team t){
		enemyTeam = t;
	}
	public Team getEnemy(){
		return enemyTeam;
	}
	public boolean isDefeated(){
		return defeated;
	}
	
	
	
	
	public void checkForHits(ArrayList<Projectile> proj){
		Player current = coach;
		while(current.getHasChild()){
			current = (Player) current.getChild();
			for(Projectile p : proj){
				p.checkForCollisionsWith(current);
			}
		}
	}
	
	public void update(){
		//enemyTeam.checkForHits(projectiles);
		
		coach.getChild().update();
		if(!coach.getHasChild()){
			defeated = true;
		}
	}
	
	public void draw(Graphics g){
		Player current = coach;
		while(current.getHasChild()){
			current = (Player) current.getChild();
			current.draw(g);
		}
	}
}
