package battle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import entities.Player;
import resources.Random;
import resources.Coordinates;
import customizables.Build;
import entities.Entity;

public class Team {
	private final String name;
	private final Color color;
	private Team enemyTeam;
	private boolean defeated;
	private ArrayList<Player> membersRem;
	
	private final int id;
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
    
    //OK to have different params, as this does not extend entity
	public void init(int y, int spacing, int facing){
		int x = spacing;
		for(Player p : membersRem){
			p.initPos(x, y, facing);
            p.doInit();
            p.setTeam(this);
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
		if(membersRem.isEmpty()){
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
	public final ArrayList<Player> getMembersRem(){
		return membersRem;
	}
	
	public void update(){
		ArrayList<Player> newMembersRem = new ArrayList<>();
		membersRem.stream().forEach(p -> {
            Entity current = p;
            while(current != null){
                current.doUpdate();
                current = current.getChild();
            }
        });
		membersRem.stream().filter(p -> !p.getShouldTerminate()).forEach(p -> newMembersRem.add(p));
		membersRem = newMembersRem;
		if(membersRem.isEmpty()){
			defeated = true;
		}
	}
	public void draw(Graphics g){
		membersRem.stream().forEach(p -> {
            Entity current = p;
            while(current != null){
                current.draw(g);
                current = current.getChild();
            }
        });
	}
}
