package battle;

import controllers.World;
import entities.AIPlayer;
import java.io.Serializable;

public class Battle implements Serializable{
	private final int baseEnemyLevel;
    private final int baseWaveSize;
    
    private int currentWave;
    
    private World host;
	private boolean end;
	
	public Battle(){
        baseEnemyLevel = 1;
        baseWaveSize = 64;
        currentWave = -1;
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
        currentWave = 1;
        spawnWave();
		end = false;
	}
    
    private void spawnWave(){
        Team ai = host.getAITeam();
        String teamName = ai.getName();
        int waveSize = (int) (baseWaveSize / Math.pow(2, currentWave - 1));
        AIPlayer p;
        
        for(int i = 0; i < waveSize; i++){
            p = new AIPlayer(String.format("%s wave %d #%d", teamName, currentWave, i), baseEnemyLevel + currentWave - 1);
            
            ai.addMember(p);
            p.setWorld(host);
            ai.initPlayer(p, host);
        }
        currentWave++;
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
        if(host.getPlayerTeam().isDefeated()){
            end = true;
        }
        if(host.getAITeam().isDefeated()){
            if(currentWave <= 9){
                spawnWave();
            } else {
                end = true;
            }
        }
    }
}
