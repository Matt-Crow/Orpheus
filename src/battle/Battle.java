package battle;

import java.awt.Color;
import java.util.ArrayList;
import initializers.Master;
import windows.BattleCanvas;

public class Battle {
	ArrayList<Team> teams;
	private Battlefield host;
	private BattleCanvas hostingCanvas;
	private boolean end;
	
	public Battle(BattleCanvas b, Team team1, Team team2){
		teams = new ArrayList<>();
		teams.add(team1);
		teams.add(team2);
		hostingCanvas = b;
	}
	
	public void setHost(Battlefield b){
		host = b;
	}
	public Battlefield getHost(){
		return host;
	}
	public BattleCanvas getCanvas(){
		return hostingCanvas;
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
}
