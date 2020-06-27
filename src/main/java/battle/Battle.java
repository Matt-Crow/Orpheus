package battle;

import world.AbstractWorld;
import entities.AIPlayer;
import java.io.Serializable;
import static java.lang.System.out;

/**
 * A battle is a gamemode where the players are pitted
 * against an army of AI.
 * 
 * As the players defeat each wave of enemies,
 * this class spawns another wave of fewer enemies, but who are more powerful.
 * 
 * This class may later be broken into separate Battle and Minigame classes.
 * 
 * @author Matt Crow
 */
public class Battle implements Serializable{
	private final int baseEnemyLevel;
    private final int maxEnemyLevel;
    
    private final int numWaves;
    
    private int currentWave;
    private AbstractWorld host;
    
    private final int WAVE_BASE = 2;
	
	public Battle(int maxEnemyLv, int waves){
        if(maxEnemyLv < 1){
            throw new IllegalArgumentException("Enemies must be at least level 1");
        }
        if(waves < 1){
            throw new IllegalArgumentException("Must have at least 1 wave");
        }
        baseEnemyLevel = 1;
        maxEnemyLevel = maxEnemyLv;
        numWaves = waves;
        currentWave = -1;
		host = null;
	}
	
	public final void setHost(AbstractWorld w){
		host = w;
	}
	public final AbstractWorld getHost(){
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
    
    /**
     * Returns the number of enemies who
     * should be spawned for a given wave
     * 
     * @param waveNum
     * @return 
     */
    private int numEnemiesForWave(int waveNum){
        int ret = 0;
        if(waveNum >= 1 && waveNum <= numWaves){
            ret = (int)Math.pow(WAVE_BASE, numWaves - waveNum + 1);
        }
        return ret;
    }
    
    /**
     * Returns the level enemies should be
     * for the given wave.
     * 
     * @param waveNum
     * @return 
     */
    private int enemyLvForWave(int waveNum){
        int ret = 0;
        if(waveNum >= 1 && waveNum <= numWaves){
            ret = baseEnemyLevel + (int)(((double)(waveNum) / numWaves) * (maxEnemyLevel - baseEnemyLevel));
        }
        return ret;
    }
    
    /**
     * Spawns new enemies into the world,
     * and heals all players in addition to clearing their damage backlog.
     */
    private void spawnWave(){
        Team ai = host.getAITeam();
        String teamName = ai.getName();
        currentWave++;
        host.getPlayerTeam().forEachMember((p)->{
            if(!p.getShouldTerminate()){
                p.getLog().clearBacklog();
                p.getLog().healPerc((double)currentWave / numWaves);
            }
        });
        int waveSize = numEnemiesForWave(currentWave); 
        waveSize *= host.getPlayerTeam().getMembersRem().size();
        AIPlayer p;
        
        for(int i = 0; i < waveSize; i++){
            p = new AIPlayer(String.format("%s wave %d #%d", teamName, currentWave, i), enemyLvForWave(currentWave));
            
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
        for(int i = 1; i < numWaves + 1; i++){
            out.printf("-Wave %d: %d enemies lv %d\n", i, numEnemiesForWave(i), enemyLvForWave(i));
        }
    }
    
    public static void main(String[] args){
        Battle b = new Battle(1, 1);
        b.displayData();
    }
}
