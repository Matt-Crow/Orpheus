package battle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import entities.Player;
import resources.Random;
import resources.Coordinates;
import resources.Direction;
import customizables.Build;

public class Team {
	private String name;
	private Color color;
	private Team enemyTeam;
	private boolean defeated;
	private ArrayList<Player> membersRem;
	
	private int id;
	private static int nextId = 0;
	
	public Team(String n, Color c){
		name = n;
		color = c;
		membersRem = new ArrayList<>();
		id = nextId;
		nextId++;
	}
	public int getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void addMember(Player m){
		membersRem.add(m);
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
		for(Player p : membersRem){
			p.init(this, x, y, new Direction(facing));
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
	public Player nearestPlayerTo(int x, int y){
		if(membersRem.size() == 0){
			throw new IndexOutOfBoundsException("No players exist for team " + name);
		}
		Player ret = membersRem.get(0);
		int distance = (int)Coordinates.distanceBetween(ret.getX(), ret.getY(), x, y);
		for(Player p : membersRem){
			int check = (int)Coordinates.distanceBetween(p.getX(), p.getY(), x, y);
			if(check < distance){
				ret = p;
				distance = check;
			}
		}
		return ret;
	}
	
	public void update(){
		ArrayList<Player> newMembersRem = new ArrayList<>();
		membersRem.stream().forEach(p -> p.update());
		membersRem.stream().filter(p -> !p.getShouldTerminate()).forEach(p -> newMembersRem.add(p));
		membersRem = newMembersRem;
		if(membersRem.size() == 0){
			defeated = true;
		}
	}
	public void draw(Graphics g){
		membersRem.stream().forEach(p -> p.draw(g));
	}
}
