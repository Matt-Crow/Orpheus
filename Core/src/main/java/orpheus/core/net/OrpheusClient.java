package orpheus.core.net;

import java.io.IOException;
import orpheus.core.users.User;

/**
 *
 * @author Matt Crow
 */
public class OrpheusClient extends AbstractNetworkClient {
    private final User user;
    private final Connection toServer;


    public OrpheusClient(User user, Connection toServer) {
        this.user = user;
        this.toServer = toServer;
        toServer.addMessageReceivedListener(mre -> this.receiveMessage(mre.getConnection(), mre.getMessage()));
        
        send(new Message(
            user.toJson().toString(), 
            ServerMessageType.PLAYER_JOINED
        ));
    }
    
    // #65 need something to call this
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
        } catch (Exception ex) {
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
