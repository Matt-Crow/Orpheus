package battle;

import controllers.World;
import entities.AbstractPlayer;
import java.io.Serializable;

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
        Team players = host.getPlayerTeam();
        Team ai = host.getAITeam();
        players.setEnemy(ai);
		players.init(host);
        ai.setEnemy(players);
		ai.init(host);
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
