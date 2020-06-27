package world;

import battle.Team;
import entities.AbstractEntity;
import entities.Projectile;

/**
 *
 * @author Matt
 */
public class RemoteProxyWorld extends AbstractWorld{

    public RemoteProxyWorld(int size) {
        super(size);
    }
    
    private void updateTeam(Team t){
        t.forEach((AbstractEntity member)->{
            if(member instanceof Projectile){
                ((Projectile)member).spawnParticles();
            }
        });
    }

    @Override
    public void update() {
        updateTeam(getPlayerTeam());
        updateTeam(getAITeam());
        updateParticles();
        updateMinigame();
    }

}
