package orpheus.core.net.protocols;

import net.messages.ServerMessageType;
import orpheus.core.commands.*;
import orpheus.core.commands.executor.LocalExecutor;
import orpheus.core.net.OrpheusServer;
import orpheus.core.net.messages.Message;
import orpheus.core.utils.timer.FrameTimer;
import world.World;

/**
 * The HostWorldProtocol is used by hosts to receive remote player controls,
 * applying the controls to players in the host's world as though the 
 * client pressed keys on the host's computer.
 * 
 * @author Matt Crow
 */
public class HostWorldProtocol extends MessageHandler {
    private final LocalExecutor executor;
    private final FrameTimer updater;
    
    public HostWorldProtocol(OrpheusServer runningServer, World forWorld){
        executor = new LocalExecutor(forWorld);
        addHandler(ServerMessageType.CONTROL_PRESSED, this::receiveControl);

        updater = new FrameTimer();
        updater.addEndOfFrameListener(e -> {
            forWorld.update();
        
            runningServer.send(new Message(
                ServerMessageType.WORLD, 
                forWorld.toGraph().toJson()
            ));
        });
    }
    
    private void receiveControl(Message sm){
        var json = sm.getBody();

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

    @Override
    public void handleStart() {
        updater.start();
    }

    @Override
    public void handleStop() {
        updater.stop();
    }
}
