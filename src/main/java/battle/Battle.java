package battle;

import controllers.World;
import entities.AIPlayer;
import java.io.Serializable;
import static java.lang.System.out;

public class Battle implements Serializable{
	private final int baseEnemyLevel;
    private final int maxEnemyLevel;
    
    private final int waveBase;
    private final int numWaves;
    
    private int currentWave;
    private World host;
	
	public Battle(){
        baseEnemyLevel = 1;
        maxEnemyLevel = 10;
        waveBase = 2;
        numWaves = 8;
        currentWave = -1;
		host = null;
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
        ai.clear();
        players.setEnemy(ai);
		players.init(host);
        ai.setEnemy(players);
		ai.init(host);
        currentWave = 0;
        spawnWave();
	}
    
    private int numEnemiesForWave(int waveNum){
        int ret = 0;
        if(waveNum >= 1 && waveNum <= numWaves){
            ret = (int)Math.pow(waveBase, numWaves - waveNum + 1);
        }
        return ret;
    }
    
    private void spawnWave(){
        Team ai = host.getAITeam();
        String teamName = ai.getName();
        currentWave++;
        host.getPlayerTeam().forEachMember((p)->{
            if(!p.getShouldTerminate()){
                p.getLog().healPerc((double)currentWave / numWaves);
            }
        });
        int waveSize = numEnemiesForWave(currentWave); 
        waveSize *= host.getPlayerTeam().getMembersRem().size();
        AIPlayer p;
        
        for(int i = 0; i < waveSize; i++){
            p = new AIPlayer(String.format("%s wave %d #%d", teamName, currentWave, i), baseEnemyLevel + currentWave - 1);
            
            ai.addMember(p);
            p.setWorld(host);
            ai.initPlayer(p, host);
        }
    }
	
    public boolean isDone(){
        //is only one team not defeated? 
        Team p = host.getPlayerTeam();
        Team i = host.getAITeam();
        return (p.isDefeated() && !i.isDefeated()) || (i.isDefeated() && !p.isDefeated());
    }
    
    public Team getWinner(){
        Team ret = null;
        if(isDone()){
            Team p = host.getPlayerTeam();
            Team i = host.getAITeam();
            ret = (p.isDefeated()) ? i : p;
        }
        return ret;
    }
    
    public void update(){
        if(host.getAITeam().isDefeated() && currentWave <= 9){
            spawnWave();
        }
    }
    
    public void displayData(){
        out.println("BATTLE");
        out.println("WAVES:");
        for(int i = 0; i < numWaves + 1; i++){
            out.printf("-Wave %d: %d enemies\n", i, numEnemiesForWave(i));
        }
    }
    
    public static void main(String[] args){
        Battle b = new Battle();
        b.displayData();
    }
}
