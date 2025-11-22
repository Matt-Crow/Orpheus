package net.protocols;

import net.OrpheusServer;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import orpheus.core.commands.*;
import orpheus.core.commands.executor.LocalExecutor;
import world.World;

/**
 * The HostWorldProtocol is used by hosts to receive remote player controls,
 * applying the controls to players in the host's world as though the 
 * client pressed keys on the host's computer.
 * 
 * Updating the world is currently handled by WorldUpdater
 * 
 * @author Matt Crow
 */
public class HostWorldProtocol extends MessageHandler {
    private final LocalExecutor executor;
    
    public HostWorldProtocol(OrpheusServer runningServer, World forWorld){
        super(runningServer);
        executor = new LocalExecutor(forWorld);
        addHandler(ServerMessageType.CONTROL_PRESSED, this::receiveControl);
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
