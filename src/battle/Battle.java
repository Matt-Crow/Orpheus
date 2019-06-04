package battle;

import controllers.World;
import java.awt.Color;
import java.util.ArrayList;
import controllers.Master;
import windows.DrawingPlane;

public class Battle {
	ArrayList<Team> teams;
	private World host;
	private final DrawingPlane hostingCanvas;
	private boolean end;
	
	public Battle(DrawingPlane b, Team team1, Team team2){
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
	public DrawingPlane getCanvas(){
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
