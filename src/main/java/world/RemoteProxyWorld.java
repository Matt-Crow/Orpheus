package world;

import battle.Team;
import entities.AbstractEntity;
import entities.Projectile;
import java.net.InetAddress;

/**
 * The RemoteProxyWorld is used by clients to represent
 * a world hosted on another computer. This handles the
 * rendering of the world, RemoteProxyProtocols handles
 * receiving messages, while RemotePlayerControls handles
 * sending messages.
 * 
 * @author Matt Crow
 * @see net.protocols.RemoteProxyProtocol
 * @see controls.RemotePlayerControls
 */
public class RemoteProxyWorld extends AbstractWorldShell{
    private final InetAddress remoteHostIp;
    
    /**
     * 
     * @param hostIp the IP address of the machine where the world this
     * is proxying is hosted.
     * @param worldContent the default contents of this world.
     */
    public RemoteProxyWorld(InetAddress hostIp, WorldContent worldContent) {
        super(worldContent);
        remoteHostIp = hostIp;
    }    
    
    /**
     * 
     * @return the IP address of the
     * machine hosting the world
     * this is serving as a proxy for.
     */
    public final InetAddress getHostIp(){
        return remoteHostIp;
    }
    
    /**
     * Spawns particles for the given team.
     * @param t 
     */
    private void updateTeam(Team t){
        t.forEach((AbstractEntity member)->{
            if(member instanceof Projectile){
                ((Projectile)member).spawnParticles();
            }
        });
    }

    /**
     * Updates all the non-serialized
     * contents of this world. Note this
     * does not affect the host in any way,
     * just this client.
     */
    @Override
    public void update() {
        updateTeam(getPlayerTeam());
        updateTeam(getAITeam());
        updateParticles();
        updateMinigame();
    }

}
