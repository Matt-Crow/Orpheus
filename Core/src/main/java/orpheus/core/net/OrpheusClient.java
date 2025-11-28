package orpheus.core.net;

import java.io.IOException;
import java.net.Socket;
import orpheus.core.users.User;

/**
 *
 * @author Matt Crow
 */
public class OrpheusClient extends AbstractNetworkClient {
    private final User user;
    private final String hostIp;
    private final int hostPort;
    
    private Connection toServer;

    public OrpheusClient(User user, SocketAddress connectTo) throws IOException {
        super();
        this.user = user;
        this.hostIp = connectTo.getAddress();
        this.hostPort = connectTo.getPort();

        // TODO encapsulate dependency on Socket
        Socket client = new Socket(hostIp, hostPort);
        toServer = new Connection(client);
        MessageListener listener = new MessageListener(toServer, this::receiveMessage);
        listener.startListening();
        send(new Message(
            user.toJson().toString(), 
            ServerMessageType.PLAYER_JOINED
        ));
    }
    
    // TODO call this somewhere
    protected void doStop() throws IOException {
        send(new Message(
            "bye",
            ServerMessageType.PLAYER_LEFT
        ));
        toServer.close();
    }

    @Override
    protected void doReceiveMessage(Connection connection, Message sm) {
        if(sm.getType() == ServerMessageType.SERVER_SHUTDOWN){
            toServer.close();
        }
    }

    @Override
    public final void send(Message sm) {
        try {
            toServer.writeServerMessage(sm.withSentBy(user));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sends a chat message to the server so it can broadcast it to other players.
     * @param message the message to send
     */
    public void sendChatMessage(String message) {
        send(new Message(
            ServerMessageType.CHAT, 
            new ChatMessage(user, message).toJson())
        );
    }
}
