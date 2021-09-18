package net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import net.connections.Connection;
import net.connections.ConnectionListener;
import net.connections.Connections;
import net.messages.MessageListener;
import net.messages.ServerMessage;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import net.protocols.AbstractOrpheusServerNonChatProtocol;
import net.protocols.ChatProtocol;
import serialization.JsonUtil;
import users.AbstractUser;
import users.LocalUser;
import util.SafeList;

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
public class OrpheusServer {
    private volatile boolean isStarted;
    private final ServerSocket server;
    
    private final Connections clients;
    private final ConnectionListener connectionHandler;    
    private final SafeList<ServerMessagePacket> cachedMessages; //messages received before the receiver could be
    
    private volatile AbstractOrpheusServerNonChatProtocol currentProtocol;
    private volatile ChatProtocol currentChatProtocol;
    
    /*
    The amount of time a call to server.accept() will block for
    */
    private static final int CONNECTION_TIME_OUT = 3000; // 3 seconds
    
    // I want to move away from using this as a singleton
    private static OrpheusServer instance = null;
    
    /**
     * Creates an OrpheusServer.
     * Note that this does not actually start the server,
     * you need to call start() for that.
     * @throws java.io.IOException
     */
    public OrpheusServer() throws IOException{
        // setting the port to 0 means "use any available port"
        server = new ServerSocket(0);
        server.setSoTimeout(CONNECTION_TIME_OUT);
        
        clients = new Connections();
        connectionHandler = new ConnectionListener(server, clients, this::setUpMessageListener);
        cachedMessages = new SafeList<>();
        
        currentProtocol = null;
        currentChatProtocol = null;
        
        isStarted = false;
    }
    
    public static final void validateServer() throws IOException{
        if(instance == null){
            instance = new OrpheusServer();
        }
    }
    
    /**
     * Use this method to interact with the OrpheusServer.
     * This method create the instance of OrpheusServer if it is not yet initialized.
     * 
     * Note that you must still call .start() on the server
     * 
     * @return the instance of OrpheusServer
     */
    public static final OrpheusServer getInstance() {
        if(instance == null){
            throw new NullPointerException("Looks like you forgot to call OrpheusServer.validateServer() before OrpheusServer.getInstance()");
        }
        return instance;
    }
        
    /**
     * 
     * @return this
     * @throws java.io.IOException
     *  
     */
    public OrpheusServer start() throws IOException{
        log("Starting OrpheusServer");
        
        if(isStarted){
            //don't start if this is already started
            log("server is already started. Ignoring invokation.");
        } else {
            log(String.format("Server initialized on %s", getConnectionString()));
            reset();
            isStarted = true;
        }
        
        return this;
    }
    
    /**
     * Clears all receivers and connections from this,
     * then restarts the connection listener.
     * 
     * @return this
     */
    public OrpheusServer reset(){
        log("Server reset, closing all connections. This is bad.");
        clients.closeAll();
        
        currentProtocol = null; 
        currentChatProtocol = null;
        
        connectionHandler.startOrContinue();
        
        return this;
    }
    
    /**
     * If the server is not started, starts it.
     * If the server is already started, resets it.
     * @return this, for chaining purposes.
     * @throws IOException if an error occurs while starting the server
     */
    public OrpheusServer restart() throws IOException{
        log("Method restart invoked");
        if(isStarted){
            reset();
        } else {
            start();
        }
        return this;
    }
    
    public final void shutDown(){
        if(!isStarted){
            return;
        }
        send(new ServerMessage(
            "server shutting down",
            ServerMessageType.SERVER_SHUTDOWN
        ));
        
        try {
            server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        isStarted = false;
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
    
    public boolean isStarted(){
        return isStarted;
    }
    
    /**
     * @param protocol the new protocol to use. This can be null
     */
    public void setProtocol(AbstractOrpheusServerNonChatProtocol protocol){
        log(String.format("Set protocol to %s", protocol.toString()));
        currentProtocol = protocol;
        cachedMessages.forEach((ServerMessagePacket sm)->{
            if(protocol.receiveMessage(sm, this)){
                cachedMessages.remove(sm);
                log("uncached message " + sm.hashCode());
            }
        });
    }
    
    /**
     * Sets the separate protocol to receive chat 
     * messages. This way, I can't have multiple
     * regular protocols, but chat also can't interfere
     * with the other protocol!
     * 
     * ... or I could just have the other protocols
     * forward to their chat...
     * 
     * @param chat the ChatProtocol to handle messages
     * received by this server.
     */
    public void setChatProtocol(ChatProtocol chat){
        log(String.format("Set chat protocol to %s", chat.toString()));
        currentChatProtocol = chat;
        cachedMessages.forEach((ServerMessagePacket sm)->{
            if(chat.receiveMessage(sm, this)){
                cachedMessages.remove(sm);
                log("uncached message " + sm.hashCode());
            }
        });
    }
    
    public synchronized void connect(String ipAddr, int port) throws UnknownHostException, IOException{
        connect(InetAddress.getByName(ipAddr), port);
    }
    
    public synchronized void connect(InetAddress ipAddr, int port) throws IOException{
        log(String.format("Connecting to %s:%d...", ipAddr, port));
        Socket sock = new Socket(ipAddr, port);
        connect(sock);
    }
    
    public synchronized void connect(Socket sock) throws IOException{
        if(!clients.isConnectedTo(sock)){
            clients.connectTo(sock);
        }
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
    
    public final void receiveMessage(ServerMessagePacket sm){
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

        boolean handled = false;
        if(currentProtocol == null){
            log("No current protocol :(");
        } else {
            handled = currentProtocol.receiveMessage(sm, this);
        }

        if(!handled && currentChatProtocol != null){
            handled = currentChatProtocol.receiveMessage(sm, this);
        }

        if(handled){
            //log("Successfully received!");
        } else {
            log("Nope, didn't receive properly, so I'll cache it: " + sm.getMessage().getBody());
            log("(" + sm.hashCode() + ")");
            cachedMessages.add(sm);

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
        os.shutDown();
    }
}
