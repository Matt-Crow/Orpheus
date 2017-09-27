package battle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import initializers.Master;

public class Battle {
	ArrayList<Team> teams;
	private Battlefield host;
	private boolean end;
	
	public Battle(Team team1, Team team2){
		teams = new ArrayList<>();
		teams.add(team1);
		teams.add(team2);
	}
	public void setHost(Battlefield b){
		host = b;
		b.setHosted(this);
	}
	public Battlefield getHost(){
		return host;
	}
	public void init(){
		int w = host.getWidth();
		int h = host.getHeight();
		int spacingFromTopEdge = host.getTileSize();
		int spacingBetween = w / 6;
		teams.get(0).init(spacingFromTopEdge, spacingBetween, 270);
		teams.get(1).init(h - spacingFromTopEdge * 2, spacingBetween, 90);
		Master.setCurrentBattle(this);
		end = false;
	}
	public boolean shouldEnd(){
		return end;
	}
	public Team getWinner(){
		for(Team t : teams){
			if(!t.isDefeated()){
				return t;
			}
		}
		return new Team("ERROR", Color.black);
	}
	public void update(){
		for(Team t : teams){
			if(t.isDefeated()){
				end = true;
			} else {
				t.update();
			}
		}
	}
	public void draw(Graphics g){
		for(Team t : teams){
			t.draw(g);
		}
	}
}
