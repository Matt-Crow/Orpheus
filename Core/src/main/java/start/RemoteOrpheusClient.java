package start;

import commands.OrpheusCommand;
import commands.SerializeableOrpheusCommand;
import net.OrpheusClient;

/**
 *
 * @author Matt Crow
 */
public class RemoteOrpheusClient extends AbstractOrpheusCommandInterpreter {
    private final OrpheusClient client;
    
    public RemoteOrpheusClient(OrpheusClient client) {
        super();
        this.client = client;
    }

    @Override
    public void doSendMessage(OrpheusCommand packet) {
        if(packet instanceof SerializeableOrpheusCommand){
            client.send(((SerializeableOrpheusCommand)packet).serialize());
        } else {
            packet.execute();
        }
    }

    @Override
    public void doReceiveMessage(OrpheusCommand packet) {
        packet.execute();
    }
}
