package orpheus.core.net;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import orpheus.core.users.User;
import serialization.JsonUtil;

/**
 * OrpheusServer is a somewhat deceptive title, as this is
 * used to establish peer-to-peer connections between players.
 * 
 * this class handles all of the interactions between computers, including
 * <ul>
 * <li>Chat</li>
 * <li>Joining a pre-match waiting room</li>
 * <li>Joining a World</li>
 * </ul>
 * 
 * Information is sent between OrpheusServers as ServerMessages,
 * encoded into JSON format, then converted into a String.
 * Upon receiving input into its socket, the server will attempt to de-serialize
 * it.
 * If the de-serialization is successful, it takes the type of that message,
 * and gives it to the current protocols
 * 
 * @author Matt Crow
 */
public class OrpheusServer extends AbstractNetworkClient {
    private final UUID uuid = UUID.randomUUID();
    private final Connections clients;
    private boolean isRunning = false;

    public OrpheusServer() {
        super();

        clients = new Connections();
        setChatMessageHandler(this::broadcastChatMessage);

        clients.closeAll();
        setMessageHandler(Optional.empty());
        isRunning = true;
    }

    public boolean isRunning() {
        return isRunning;
    }

    private final void broadcastChatMessage(ChatMessage chatMessage) {
        var sendMe = new Message(ServerMessageType.CHAT, chatMessage.toJson());
        sendToAllExcept(sendMe, chatMessage.getSender());
    }

    // TODO call this somewhere
    protected final void doStop() {
        if (!isRunning) {
            return;
        }
        send(new Message(ServerMessageType.SERVER_SHUTDOWN));
        isRunning = false;
    }

    @Override
    public final void send(Message sm) {
        try {
            clients.broadcast(sm);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendToAllExcept(Message message, User user) {
        try {
            clients.sendToAllExcept(message, user);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public final boolean send(Message sm, User recipient) {
        boolean success = false;
        if (clients.isConnectedTo(recipient)) {
            try {
                clients.getConnectionTo(recipient).writeServerMessage(sm);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            success = true;
        } else {
            System.err.printf("Not connected to %s. Here are my connections:\n%s\n", recipient, clients);
        }
        return success;
    }

    @Override
    protected final void doReceiveMessage(Connection connection, Message sm) {
        // handle joining / leaving
        if (sm.getType() == ServerMessageType.PLAYER_JOINED) {
            receiveJoin(connection, sm);
        } else if (sm.getType() == ServerMessageType.PLAYER_LEFT) {
            clients.disconnectFrom(connection);
        }
    }

    private void receiveJoin(Connection connection, Message sm) {
        User sender = User.fromJson(JsonUtil.fromString(sm.getBodyText()));
        connection.setRemoteUser(sender);
        log(clients);
    }

    public void connectTo(Connection connection) {
        clients.connectTo(connection);
        
        log("Opening message listener thread...");
        new MessageListener(connection, this::receiveMessage).startListening();
        log(clients);
    }

    // make this save to a file later
    public final void log(String msg) {
        System.out.println("OrpheusServer: " + msg);
    }

    public final void log(Object obj) {
        log(obj.toString());
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(String.format("Orpheus Server %s\n", uuid));
        sb.append(clients.toString());
        return sb.toString();
    }
}
