package start;

import users.AbstractUser;
import commands.OrpheusCommand;

/**
 *
 * @author Matt Crow
 */
public class SoloOrpheusClient extends AbstractOrpheusClient {
    public SoloOrpheusClient(AbstractUser user){
        super(user);
    }

    @Override
    public void doSendMessage(OrpheusCommand packet) {
        doReceiveMessage(packet);
    }

    @Override
    public void doReceiveMessage(OrpheusCommand packet) {
        packet.execute();
    }
}
