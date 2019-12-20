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
        Team players = host.getPlayerTeam();
        Team ai = host.getAITeam();
        int spacingBetween = s / (ai.getRosterSize() + 1);
        players.setEnemy(ai);
		players.init(spacingFromTopEdge, spacingBetween, 270);
        ai.setEnemy(players);
		ai.init(s - spacingFromTopEdge * 2, spacingBetween, 90);
		end = false;
	}
	
	public boolean shouldEnd(){
		return end;
	}
    public boolean checkIfOver(){
        //is only one team not defeated? 
        Team p = host.getPlayerTeam();
        Team i = host.getAITeam();
        return (p.isDefeated() && !i.isDefeated()) || (i.isDefeated() && !p.isDefeated());
    }
    
    public Team getWinner(){
        Team ret = null;
        if(end && checkIfOver()){
            Team p = host.getPlayerTeam();
            Team i = host.getAITeam();
            ret = (p.isDefeated()) ? i : p;
        }
        return ret;
    }
    
    public void update(){
        if(checkIfOver()){
            end = true;
        }
    }
}
