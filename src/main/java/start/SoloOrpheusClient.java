package start;

import net.messages.ServerMessagePacket;
import users.AbstractUser;

/**
 *
 * @author Matt Crow
 */
public class SoloOrpheusClient extends AbstractOrpheusClient {
    public SoloOrpheusClient(AbstractUser user){
        super(user);
    }

    @Override
    public void doSendMessage(ServerMessagePacket packet) {
        doReceiveMessage(packet);
    }

    @Override
    public void doReceiveMessage(ServerMessagePacket packet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
