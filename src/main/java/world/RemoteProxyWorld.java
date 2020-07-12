package world;

import battle.Team;
import controllers.Master;
import entities.AbstractEntity;
import entities.Projectile;
import java.io.IOException;
import java.io.Serializable;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;
import net.OrpheusServer;
import net.ServerMessage;
import net.ServerMessageType;
import util.SerialUtil;

/**
 *
 * @author Matt
 */
public class RemoteProxyWorld extends AbstractWorld{
    private String remoteHostIp;
    private final Consumer<ServerMessage> receiveWorldUpdate;
    
    public RemoteProxyWorld(WorldContent worldContent) {
        super(worldContent);
        remoteHostIp = "";
        receiveWorldUpdate = (Consumer<ServerMessage> & Serializable)(sm)->receiveWorldUpdate(sm);
    }
    
    public final void setRemoteHost(String ipAddr) throws IOException{
        OrpheusServer server = OrpheusServer.getInstance();
        if(!server.isStarted()){
            server.start();
        }
        
        remoteHostIp = ipAddr;
        server.addReceiver(ServerMessageType.WORLD_UPDATE, receiveWorldUpdate);
    }
    
    public final String getHostIp(){
        return remoteHostIp;
    }
    
    private void receiveWorldUpdate(ServerMessage sm){
        //synchronized(teams){
        SwingUtilities.invokeLater(()->{
            Team[] ts = (Team[])SerialUtil.fromSerializedString(sm.getBody());
            setPlayerTeam(ts[0]);
            setEnemyTeam(ts[1]);

            Master.getUser().linkToRemotePlayerInWorld(this); //since teams have changed
        });
        
        //}
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
