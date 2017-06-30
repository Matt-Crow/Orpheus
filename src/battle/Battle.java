package battle;

import java.awt.Graphics;
import java.util.ArrayList;

import entities.*;

public class Battle {
	ArrayList<Team> teams;
	private Battlefield host;
	
	public Battle(Team team1, Team team2){
		teams = new ArrayList<>();
		teams.add(team1);
		teams.add(team2);
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
