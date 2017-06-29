package battle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import entities.*;
import initializers.Run;

public class Battle {
	ArrayList<Team> teams;
	private Battlefield host;
	
	public Battle(){
		teams = new ArrayList<>();
		
		Team team1 = new Team("Team 1", Color.red);
		Team team2 = new Team("Team 2", Color.green);
		
		team1.addMember(Run.player);
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
		
		team1.setEnemy(team2);
		team2.setEnemy(team1);
		
		String c = Run.player.getCharacterClass().getName();
		for(Team t : teams){
			t.setAllClassesToRandom();
		}
		Run.player.setClass(c);
	}
	public void setHost(Battlefield b){
		host = b;
		b.setHosted(this);
	}
	public void init(){
		int w = host.getWidth();
		int h = host.getHeight();
		int spacingFromTopEdge = host.getTileSize();
		int spacingBetween = w / 6;
		teams.get(0).init(spacingFromTopEdge, spacingBetween, 4);
		teams.get(1).init(h - spacingFromTopEdge * 2, spacingBetween, 0);
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
