package battle;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;

import entities.Player;
import entities.Projectile;
import resources.Coordinates;
import resources.Random;
import resources.Direction;
import customizables.Build;

public class Team {
	private String name;
	private Color color;
	private ArrayList<Player> members;
	private ArrayList<Projectile> projectiles;
	private ArrayList<Projectile> newProjectiles;
	private Team enemyTeam;
	private boolean defeated;
	
	public Team(String n, Color c){
		name = n;
		color = c;
		members = new ArrayList<>();
	}
	public String getName(){
		return name;
	}
	public Color getColor(){
		return color;
	}
	public ArrayList<Coordinates> getAllCoords(){
		ArrayList<Coordinates> ret = new ArrayList<>();
		for(Player p : members){
			ret.add(p.getCoords());
		}
		return ret;
	}
	
	public void addMember(Player m){
		if(!members.contains(m)){
			members.add(m);
		}
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
		for(Player m : members){
			m.init(this, x, y, new Direction(facing));
			x += spacing;
		}
		projectiles = new ArrayList<>();
		newProjectiles = new ArrayList<>();
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
	
	public void registerProjectile(Projectile p){
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
			if(!m.getShouldTerminate()){
				newTeam.add(m);
			}
		}
		members = newTeam;
		
		if(members.size() == 0){
			defeated = true;
		}
		
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
}
