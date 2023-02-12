package start;

import commands.OrpheusCommand;

/**
 * immediately forwards commands to itself
 * @author Matt Crow
 */
public class SoloOrpheusCommandInterpreter extends AbstractOrpheusCommandInterpreter {

    @Override
    public void doSendMessage(OrpheusCommand packet) {
        doReceiveMessage(packet);
    }

    @Override
    public void doReceiveMessage(OrpheusCommand packet) {
        packet.execute();
    }
}
