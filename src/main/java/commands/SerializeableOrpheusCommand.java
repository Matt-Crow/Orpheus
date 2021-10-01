package commands;

import net.messages.ServerMessage;

/**
 * Needs a static method or another class to de-serialize
 * @author Matt Crow
 */
public interface SerializeableOrpheusCommand extends OrpheusCommand {
    public ServerMessage serialize();
}
