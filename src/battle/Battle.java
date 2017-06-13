package battle;

import java.awt.Color;
import java.util.ArrayList;

import entities.Player;

public class Battle {
	ArrayList<Team> teams;
	
	public Battle(){
		teams = new ArrayList<>();
		
		Team team1 = new Team("Team 1", Color.red);
		Team team2 = new Team("Team 2", Color.blue);
		
		team1.addMember(new Player("Matt"));
		team1.addMember(new Player("Nick"));
		team1.addMember(new Player("Paul"));
		team1.addMember(new Player("Gianna"));
		team1.addMember(new Player("David"));
		
		team2.addMember(new Player("Grunt #1"));
		team2.addMember(new Player("Grunt #2"));
		team2.addMember(new Player("Grunt #3"));
		team2.addMember(new Player("Grunt #4"));
		team2.addMember(new Player("Grunt #5"));
		
		teams.add(team1);
		teams.add(team2);
		
		init();
	}
	
	public void init(){
		for(Team t : teams){
			t.init();
		}
	}
	
	public void update(){
		for(Team t : teams){
			t.update();
		}
	}
	
	public Player getPlayer(){
		return teams.get(0).getMember(0);
	}
}
