package world;

import battle.Team;
import entities.AbstractEntity;
import entities.AbstractPlayer;
import entities.Projectile;

/**
 *
 * @author Matt
 */
public class SoloWorld extends AbstractWorldShell{

    public SoloWorld(WorldContent worldContent) {
        super(worldContent);
    }
    
    private void updateTeam(Team t){
        t.update();
        t.forEach((AbstractEntity member)->{
            getMap().checkForTileCollisions(member);
            if(t.getEnemy() != null){
                t.getEnemy().getMembersRem().forEach((AbstractPlayer enemy)->{
                    if(member instanceof Projectile){
                        // I thought that java handled this conversion?
                        ((Projectile)member).checkForCollisions(enemy);
                    }
                    //member.checkForCollisions(enemy);
                });
            }
        });
    }
    
    
    @Override
    public void update() {
        // update each team
        updateTeam(getPlayerTeam());
        updateTeam(getAITeam());
        updateParticles();
        updateMinigame();
    }

}
