package commands;

import orpheus.core.net.messages.Message;

/**
 * Needs a static method or another class to de-serialize
 * @author Matt Crow
 */
public interface SerializeableOrpheusCommand extends OrpheusCommand {
    public Message serialize();
}
