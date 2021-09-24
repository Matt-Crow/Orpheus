package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import net.connections.Connection;
import net.connections.ConnectionListener;
import net.connections.Connections;
import net.messages.MessageListener;
import net.messages.ServerMessage;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import serialization.JsonUtil;
import users.AbstractUser;
import users.LocalUser;

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
 * Upon receiving input into its socket, the server will attempt to de-serialize it.
 * If the de-serialization is successful, it takes the type of that message,
 * and gives it to the current protocols
 * 
 * @see net.protocols.AbstractOrpheusServerNonChatProtocol
 * 
 * @author Matt Crow
 */
public class OrpheusServer extends AbstractNetworkClient {
    private final ServerSocket server;
    private final Connections clients;
    private final ConnectionListener connectionHandler;    
    
    /*
    The amount of time a call to server.accept() will block for
    */
    private static final int CONNECTION_TIME_OUT = 3000; // 3 seconds
    
    /**
     * Creates an OrpheusServer.
     * Note that this does not actually start the server,
     * you need to call start() for that.
     * @throws java.io.IOException
     */
    public OrpheusServer() throws IOException{
        super();
        // setting the port to 0 means "use any available port"
        server = new ServerSocket(0);
        server.setSoTimeout(CONNECTION_TIME_OUT);
        
        clients = new Connections();
        connectionHandler = new ConnectionListener(server, clients, this::setUpMessageListener);
    }
        
    /**
     * Clears all receivers and connections from this,
     * then restarts the connection listener.
     * 
     * @throws java.io.IOException
     */
    @Override
    protected final void doStart() throws IOException{
        log(String.format("Server initialized on %s", getConnectionString()));
        clients.closeAll();
        clearProtocols();
        
        connectionHandler.startOrContinue();
    }
    
    @Override
    protected final void doStop() throws IOException {
        send(new ServerMessage(
            "server shutting down",
            ServerMessageType.SERVER_SHUTDOWN
        ));
        
        server.close();
    }
    
    /**
     * @return the address of the server in the form IP:PORT 
     */
    public final String getConnectionString(){
        return String.format("%s:%d", server.getInetAddress().getHostAddress(), getPort());
    }
    
    public final String getIpAddress(){
        return server.getInetAddress().getHostAddress();
    }
    
    public final int getPort(){
        return server.getLocalPort();
    }
    
    private void setUpMessageListener(Connection conn){
        log("Opening message listener thread...");
        new MessageListener(conn, this::receiveMessage).startListening();
        
        //includes the User data so the other computer has access to username
        
        conn.writeServerMessage(new ServerMessage(
            LocalUser.getInstance().serializeJson().toString(),
            ServerMessageType.PLAYER_JOINED
        ));        
        
        log("Wrote user information to client");
        log(clients);
    }
    
    @Override
    public final void send(ServerMessage sm){
        clients.broadcast(sm);
    }
    
    public final boolean send(ServerMessage sm, AbstractUser recipient){
        boolean success = false;
        if(clients.isConnectedTo(recipient)){
            clients.getConnectionTo(recipient).writeServerMessage(sm);
            success = true;
        } else {
            System.err.printf("Not connected to %s. Here are my connections:\n%s\n", recipient, clients);
        }
        return success;
    }
    
    @Override
    protected final void doReceiveMessage(ServerMessagePacket sm){
        if(clients.isConnectedTo(sm.getSendingSocket())){
           sm.setSender(clients.getConnectionTo(sm.getSendingSocket()).getRemoteUser()); 
        } else {
           log("I don't recognize " + sm.getSendingSocket());
        }

        // handle joining / leaving
        if(sm.getMessage().getType() == ServerMessageType.PLAYER_JOINED){
            receiveJoin(sm);
        } else if (sm.getMessage().getType() == ServerMessageType.PLAYER_LEFT){
            clients.disconnectFrom(sm.getSendingSocket());
        }
    }
    
    private void receiveJoin(ServerMessagePacket sm){
        Socket ip = sm.getSendingSocket(); // needs this, as the remote user is not yet set
        boolean isConnected = clients.isConnectedTo(ip);
        if(isConnected && clients.getConnectionTo(ip).getRemoteUser() != null){
            log("already connected");
        } else if(isConnected){
            //connected to IP, but no user data set yet
            AbstractUser sender = AbstractUser.deserializeJson(JsonUtil.fromString(sm.getMessage().getBody()));      
            sm.setSender(sender);
            clients.getConnectionTo(ip).setRemoteUser(sender);
        } else {
            //not connected, no user data
            try {
                connect(sm.getSendingSocket());
                AbstractUser sender = AbstractUser.deserializeJson(JsonUtil.fromString(sm.getMessage().getBody()));
                sm.setSender(sender);
                clients.getConnectionTo(ip).setRemoteUser(sender);
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
        log(clients);
    }
    
    private synchronized void connect(Socket sock) throws IOException{
        if(!clients.isConnectedTo(sock)){
            clients.connectTo(sock);
        }
    }
    
    // make this save to a file later
    public final void log(String msg){
        System.out.println("OrpheusServer: " + msg);
    }
    
    public final void log(Object obj){
        log(obj.toString());
    }
    
    // yay! this works!
    public static void main(String[] args) throws Exception{
        OrpheusServer os = new OrpheusServer();
        System.out.println(os.clients);
        os.start();
        
        new Thread(){
            @Override
            public void run(){
                try {
                    Connection conn = new Connection(new Socket(os.getIpAddress(), os.getPort()));
                    Thread.sleep(1000);
                    conn.writeServerMessage(new ServerMessage("bye", ServerMessageType.PLAYER_LEFT));
                    conn.close();
                    System.out.println("Bye");
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(5000);
        System.out.println(os.clients);
        os.stop();
    }
}
