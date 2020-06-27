package world;

import battle.Team;
import controllers.Master;
import entities.AbstractEntity;
import entities.AbstractPlayer;
import entities.HumanPlayer;
import controls.PlayerControls;
import entities.Projectile;
import java.io.IOException;
import java.io.Serializable;
import java.util.function.Consumer;
import net.ServerMessage;
import net.ServerMessageType;
import util.SerialUtil;

/**
 *
 * @author Matt
 */
public class HostWorld extends AbstractWorld{
    private final Consumer<ServerMessage> receiveControl;
    
    public HostWorld(WorldContent worldContent) {
        super(worldContent);
        receiveControl = (Consumer<ServerMessage> & Serializable) (sm)->receiveControl(sm);
    }
    
    public final void initServer() throws IOException{
        if(!Master.SERVER.isStarted()){
            Master.SERVER.start();
        }
        Master.SERVER.addReceiver(ServerMessageType.CONTROL_PRESSED, receiveControl);
    }
    
    private void receiveControl(ServerMessage sm){
        HumanPlayer p = sm.getSender().getPlayer();
        PlayerControls.decode(p, sm.getBody());
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
