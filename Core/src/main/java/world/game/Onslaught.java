package world.game;

import orpheus.core.world.occupants.players.Player;
import world.World;
import world.battle.Team;

/**
 * players are pitted against an AI team
 * new enemies are spawned in waves once the current wave is defeated
 * once the players defeat all waves, they win!
 * 
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class Onslaught implements Game {    
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
        var players = host.getPlayers();
        var ai = host.getAi();
        ai.clear();
        
        players.setEnemy(ai);
        ai.setEnemy(players);
        
        currentWave = 0;
        spawnWave();
    }
    
    private void spawnWave(){
        Team ai = host.getAi();
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
        for(int i = 0; i < waveSize; i++){
            var p = Player.makeDrone(host, currentWave);
            
            ai.addAiMember(p);
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
