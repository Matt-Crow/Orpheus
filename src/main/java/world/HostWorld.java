package world;

import world.battle.Team;
import world.entities.AbstractEntity;
import world.entities.AbstractPlayer;
import world.entities.Projectile;
import net.OrpheusServer;
import net.messages.ServerMessage;
import net.messages.ServerMessageType;
import util.SerialUtil;

/**
 * The HostWorld is used in conjunction with
 * HostWorldProtocol to 
 * 1. Update local world contents, 
 * 2. serialize and send those contents to all clients so they can update their proxies,
 * 3. while the protocol handles receiving controls from remote players.
 * @author Matt
 */
public class HostWorld extends AbstractWorldShell{
    public HostWorld(WorldContent worldContent) {
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
    
    /**
     * Updates the local world,
     * serializes it, and sends 
     * it to all connected clients
     * so they can update their proxies.
     */
    @Override
    public void update() {
        updateTeam(getPlayerTeam());
        updateTeam(getAITeam());
        updateParticles();
        updateMinigame();
        OrpheusServer.getInstance().send(new ServerMessage(
            SerialUtil.serializeToString(getContent()),
            ServerMessageType.WORLD_UPDATE
        ));
    }

}
