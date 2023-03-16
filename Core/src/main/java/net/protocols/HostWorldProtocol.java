package net.protocols;

import net.OrpheusServer;
import net.messages.ServerMessagePacket;
import orpheus.core.commands.*;
import orpheus.core.commands.executor.LocalExecutor;
import world.World;

/**
 * The HostWorldProtocol is used by hosts to receive remote player controls,
 * applying the controls to players in the host's world as though the 
 * client pressed keys on the host's computer.
 * 
 * Updating the world is currently handled by HostWorldUpdater
 * 
 * @author Matt Crow
 */
public class HostWorldProtocol extends AbstractProtocol {
    private final LocalExecutor executor;
    
    public HostWorldProtocol(OrpheusServer runningServer, World forWorld){
        super(runningServer);
        executor = new LocalExecutor(forWorld);
    }
    
    @Override
    public boolean receive(ServerMessagePacket sm) {
        boolean handled = true;
        switch(sm.getMessage().getType()){
            case CONTROL_PRESSED:
                receiveControl(sm);
                break;
            default:
                handled = false;
                break;
        }
        return handled;
    }
    
    private void receiveControl(ServerMessagePacket sm){
        var json = sm.getMessage().getBody();

        switch (json.getString("type")) {
            case "StartMoving": {
                executor.execute(StartMoving.fromJson(json));
                break;
            }
            case "StopMoving": {
                executor.execute(StopMoving.fromJson(json));
                break;
            }
            case "TurnTo": {
                executor.execute(TurnTo.fromJson(json));
                break;
            }
            case "UseActive": {
                executor.execute(UseActive.fromJson(json));
                break;
            }
            case "UseCantrip": {
                executor.execute(UseCantrip.fromJson(json));
                break;
            }
            default: {
                throw new UnsupportedOperationException(String.format("Unrecognized command type: \"%s\"", json.getString("type")));
            }
        }
    }
}
