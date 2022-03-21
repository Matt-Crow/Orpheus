package world.game;

import java.io.Serializable;
import world.World;
import world.battle.Team;
import world.entities.AIPlayer;

/**
 * players are pitted against an AI team
 * new enemies are spawned in waves once the current wave is defeated
 * once the players defeat all waves, they win!
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class Onslaught implements Game, Serializable {    
    private World host;
    private final int numWaves;
    private int currentWave;
    
    public Onslaught(int numWaves){
        if(numWaves <= 0){
            throw new IllegalArgumentException(String.format("numWaves must be positive, not %d", numWaves));
        }
        this.numWaves = numWaves;
    }
    
    @Override
    public void setHost(World host){
        if(host == null){
            throw new IllegalArgumentException("host cannot be null");
        }
        
        this.host = host;
    }

    @Override
    public void play() {
        Team players = host.getPlayers();
        Team ai = host.getAi();
        ai.clear();
        
        players.setEnemy(ai);
        ai.setEnemy(players);
        
        players.init(host);
        ai.init(host);
        
        currentWave = 0;
        spawnWave();
    }
    
    private void spawnWave(){
        Team ai = host.getAi();
        String teamName = ai.getName();
        ++currentWave;
        /*
        wave | size
           1     3
           2     5
           3     6
           4     7
           5     8
          10    10
        */
        int waveSize = (int) Math.ceil(4 * Math.log(currentWave + 1));
        AIPlayer p;
        for(int i = 0; i < waveSize; i++){
            p = new AIPlayer(
                host,
                String.format("%s wave %d #%d", teamName, currentWave, i), 
                currentWave
            );
            
            ai.addMember(p);
            ai.initPlayer(p, host);
        }
    }

    @Override
    public void update() {
        if(host.getAi().isDefeated() && currentWave < numWaves){
            spawnWave();
        }
    }

    @Override
    public boolean isOver() {
        return host.getPlayers().isDefeated() || host.getAi().isDefeated();
    }

    @Override
    public boolean isPlayerWin() {
        return isOver() && !host.getPlayers().isDefeated();
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Onslaught:%n"));
        sb.append(String.format("\tnumWaves: %d%n", numWaves));
        sb.append(String.format("\tcurrentWave: %d", currentWave));
        return sb.toString();
    }
}
