package world;

import battle.Team;
import controllers.Master;
import entities.AbstractEntity;
import entities.AbstractPlayer;
import entities.Projectile;
import net.ServerMessage;
import net.ServerMessageType;
import util.SerialUtil;

/**
 *
 * @author Matt
 */
public class HostWorld extends AbstractWorld{

    public HostWorld(int size) {
        super(size);
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
        updateTeam(getPlayerTeam());
        updateTeam(getAITeam());
        Master.SERVER.send(new ServerMessage(
            SerialUtil.serializeToString(new Team[]{getPlayerTeam(), getAITeam()}),
            ServerMessageType.WORLD_UPDATE
        ));
        updateParticles();
        updateMinigame();
    }

}
