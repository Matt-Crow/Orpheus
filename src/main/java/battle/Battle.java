package battle;

import controllers.World;
import entities.AbstractPlayer;
import java.io.Serializable;
import java.util.Arrays;

public class Battle implements Serializable{
	private World host;
	private boolean end;
	
	public Battle(){
		host = null;
        end = false;
	}
	
	public void setHost(World w){
		host = w;
	}
	public World getHost(){
		return host;
	}
	
	public void init(){
		int s = host.getMap().getWidth();
		int spacingFromTopEdge = AbstractPlayer.RADIUS;
        Team[] teams = host.getTeams();
        int spacingBetween = s / (teams[0].getRosterSize() + 1);
        teams[0].setEnemy(teams[1]);
		teams[0].init(spacingFromTopEdge, spacingBetween, 270);
        teams[1].setEnemy(teams[0]);
		teams[1].init(s - spacingFromTopEdge * 2, spacingBetween, 90);
		end = false;
	}
	
	public boolean shouldEnd(){
		return end;
	}
    public boolean checkIfOver(){
        //is only one team not defeated? 
        return Arrays
            .stream(host.getTeams())
            .filter((Team t)->!t.isDefeated())
            .toArray(size -> new Team[size])
            .length == 1;
    }
    
    public Team getWinner(){
        Team ret = null;
        if(end){
            ret = Arrays
            .stream(host.getTeams())
            .filter((Team t)->!t.isDefeated())
            .findFirst().get();
        }
        return ret;
    }
    
    public void update(){
        if(checkIfOver()){
            end = true;
        }
    }
}
