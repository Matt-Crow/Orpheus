package battle;

import java.util.ArrayList;
import java.awt.Color;

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
	
	public void init(){
		for(Player m : members){
			m.init();
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
