package battle;

import java.awt.Color;
import java.util.ArrayList;
import entities.Player;
import resources.Random;
import resources.Direction;
import customizables.Build;

public class Team {
	private String name;
	private Color color;
	private Team enemyTeam;
	private boolean defeated;
	private ArrayList<Player> roster;
	private int membersRem;
	
	public Team(String n, Color c){
		name = n;
		color = c;
		roster = new ArrayList<>();
		membersRem = 0;
	}
	public String getName(){
		return name;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void addMember(Player m){
		roster.add(m);
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
		for(Player p : roster){
			p.init(this, x, y, new Direction(facing));
			x += spacing;
		}
		membersRem = roster.size();
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
	
	public void update(){
		if(membersRem == 0){
			defeated = true;
		}
	}
}
