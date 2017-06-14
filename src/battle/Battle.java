package battle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import entities.Player;

public class Battle {
	ArrayList<Team> teams;
	
	public Battle(){
		teams = new ArrayList<>();
		
		Team team1 = new Team("Team 1", Color.red);
		Team team2 = new Team("Team 2", Color.green);
		
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
	}
	
	public void loadCoords(int w, int h){
		int spacingFromTopEdge = 100;
		int spacingBetween = w / 6;
		teams.get(0).loadCoords(spacingFromTopEdge, spacingBetween);
		teams.get(1).loadCoords(h - spacingFromTopEdge * 2, spacingBetween);
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
	public void draw(Graphics g){
		for(Team t : teams){
			t.draw(g);
		}
	}
	
	public Player getPlayer(){
		return teams.get(0).getMember(0);
	}
}
