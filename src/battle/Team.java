package battle;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;

import entities.Player;
import entities.Projectile;
import resources.Op;

public class Team {
	public String name;
	public Color color;
	private ArrayList<Player> members;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Projectile> newProjectiles;
	private Team enemyTeam;
	
	public Team(String n, Color c){
		name = n;
		color = c;
		members = new ArrayList<>();
		projectiles = new ArrayList<>();
	}
	
	public void addMember(Player m){
		members.add(m);
	}
	
	public void setAllClassesToRandom(){
		for(Player m : members){
			m.setClass("random");
		}
	}
	
	public void init(int y, int spacing, int facing){
		int x = spacing;
		for(Player m : members){
			m.init(this, x, y, facing);
			x += spacing;
		}
		projectiles = new ArrayList<>();
		newProjectiles = new ArrayList<>();
	}
	public void setEnemy(Team t){
		enemyTeam = t;
	}
	public void registerProjectile(Projectile p){
		projectiles.add(p);
	}
	public void registerAOEProjectile(Projectile p){
		newProjectiles.add(p);
	}
	public void checkForHits(ArrayList<Projectile> proj){
		for(Player m : members){
			for(Projectile p : proj){
				p.checkForCollisionsWith(m);
			}
		}
	}
	
	public void update(){
		enemyTeam.checkForHits(projectiles);
		
		ArrayList<Player> newTeam = new ArrayList<>();
		for(Player m : members){
			m.update();
			/*
			 * if(NOT DEAD){
			 * 	newTeam.add(m);
			 * }
			 */
		}
		// members = newTeam;
		
		for(Projectile p : projectiles){
			p.update();
			if(!p.getShouldTerminate()){
				newProjectiles.add(p);
			}
		}
		projectiles = newProjectiles;
		newProjectiles = new ArrayList<>();
	}
	
	public void draw(Graphics g){
		for(Player m : members){
			m.draw(g);
		}
		for(Projectile p : projectiles){
			p.draw(g);
		}
	}
	
	public Player getMember(int index){
		return members.get(index);
	}
	
	public void displayData(){
		Op.add(name);
		for(Player m : members){
			Op.add("* " + m.name);
		}
		Op.dp();
	}
}
