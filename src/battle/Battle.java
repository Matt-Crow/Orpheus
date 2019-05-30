package battle;

import controllers.World;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import initializers.Master;
import windows.BattleCanvas;

public class Battle {
	ArrayList<Team> teams;
	private World host;
	private final BattleCanvas hostingCanvas;
	private boolean end;
	
	public Battle(BattleCanvas b, Team team1, Team team2){
		teams = new ArrayList<>();
		teams.add(team1);
		teams.add(team2);
		hostingCanvas = b;
	}
	
	public void setHost(World w){
		host = w;
	}
	public World getHost(){
		return host;
	}
	public BattleCanvas getCanvas(){
		return hostingCanvas;
	}
	
	public void init(){
		int s = host.getSize();
		int spacingFromTopEdge = 50; //arbitrary. Will change later
		int spacingBetween = s / 6;
		teams.get(0).init(spacingFromTopEdge, spacingBetween, 270);
		teams.get(1).init(s - spacingFromTopEdge * 2, spacingBetween, 90);
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
            }
        }
	}
}
