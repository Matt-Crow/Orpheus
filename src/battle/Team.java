package battle;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;

import entities.Player;
import entities.Projectile;
import resources.Op;
import resources.Coordinates;
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
		projectiles = new ArrayList<>();
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
	public Player getPlayerByCoords(Coordinates c){
		for(Player p : members){
			if(p.getCoords().getX() == c.getX() && p.getCoords().getY() == c.getY()){
				return p;
			}
		}
		Op.dp();
		return new Player("ERROR");
	}
	public void addMember(Player m){
		members.add(m);
	}
	
	public void applyBuilds(Build[] b){
		for(int i = 0; i < members.size(); i++){
			members.get(i).applyBuild(b[i]);;
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
	
	public Player getMember(int index){
		return members.get(index);
	}
	
	public void displayData(){
		Op.add(name);
		for(Player m : members){
			Op.add("* " + m.getName());
		}
		Op.dp();
	}
}
