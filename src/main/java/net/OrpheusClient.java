package net;

import java.io.IOException;
import java.net.Socket;
import net.connections.Connection;
import net.messages.MessageListener;
import net.messages.ServerMessage;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import users.LocalUser;

/**
 *
 * @author Matt Crow
 */
public class OrpheusClient extends AbstractNetworkClient {
    private final String hostIp;
    private final int hostPort;
    
    private Connection toServer;
    
    public OrpheusClient(String hostIp, int hostPort){
        super();
        this.hostIp = hostIp;
        this.hostPort = hostPort;
    }
    
    @Override
    protected void doStart() throws IOException {
        Socket client = new Socket(hostIp, hostPort);
        toServer = new Connection(client);
        MessageListener listener = new MessageListener(toServer, this::receiveMessage);
        listener.startListening();
        send(new ServerMessage(
            LocalUser.getInstance().serializeJson().toString(), 
            ServerMessageType.PLAYER_JOINED
        ));
    }

    @Override
    protected void doStop() throws IOException {
        send(new ServerMessage(
            "bye",
            ServerMessageType.PLAYER_LEFT
        ));
        toServer.close();
    }

    @Override
    protected void doReceiveMessage(ServerMessagePacket sm) {
        if(sm.getMessage().getType() == ServerMessageType.SERVER_SHUTDOWN){
            toServer.close();
        }
    }

    @Override
    public final void send(ServerMessage sm) {
        try {
            toServer.writeServerMessage(sm);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
