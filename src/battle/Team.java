package battle;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;

import entities.Player;
import resources.Op;

public class Team {
	public String name;
	public Color color;
	private ArrayList<Player> members;
	private Team enemyTeam;
	
	public Team(String n, Color c){
		name = n;
		color = c;
		members = new ArrayList<>();
	}
	
	public void addMember(Player m){
		members.add(m);
	}
	public void setAllClassesToRandom(){
		for(Player m : members){
			m.setClass("random");
		}
	}
	public void loadCoords(int y, int spacing){
		int x = spacing;
		for(Player m : members){
			m.setCoords(x, y);
			x += spacing;
		}
	}
	public void init(){
		for(Player m : members){
			m.init(this);
		}
	}
	
	public void setEnemy(Team t){
		enemyTeam = t;
	}
	
	public void update(){
		for(Player m : members){
			m.update();
		}
	}
	
	public void draw(Graphics g){
		for(Player m : members){
			m.draw(g);
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
