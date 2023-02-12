package start;

import commands.OrpheusCommand;

/**
 * The AbstractOrpheusClient provides a layer of abstraction to how the program
 * reacts based on whether the user is playing a multi-player or single-player
 * game
 * 
 * @author Matt Crow
 */
public abstract class AbstractOrpheusCommandInterpreter {
    
    public final void execute(OrpheusCommand cmd){
        doSendMessage(cmd);
    }
    
    public final void receiveMessage(OrpheusCommand packet){
        doReceiveMessage(packet);
    }
    
    public abstract void doSendMessage(OrpheusCommand packet);
    public abstract void doReceiveMessage(OrpheusCommand packet);
}
