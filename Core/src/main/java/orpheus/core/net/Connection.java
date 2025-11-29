package orpheus.core.net;

import orpheus.core.users.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Represents a connection to either a client or a server running Orpheus
 * @author Matt Crow
 */
public class Connection {

    /**
     * the user playing Orpheus on the machine this connects to
     */
    private User remoteUser;
    
    private final Socket clientSocket;    
    private final BufferedReader fromClient;
    private final BufferedWriter toClient;
    
    private Connection(Socket s, OutputStream os, InputStream is){
        clientSocket = s;
        toClient = new BufferedWriter(new OutputStreamWriter(os));
        fromClient = new BufferedReader(new InputStreamReader(is));
    }

    /**
     * Attempts to establish a Connection to the given remote address
     * @param socketAddress the address to connect to
     * @return a connection to the given address
     * @throws IOException if any exceptions occur when connection to the given address
     */
    public static Connection forRemote(SocketAddress socketAddress) throws IOException {
        var socket = new Socket(socketAddress.getAddress(), socketAddress.getPort());
        return Connection.forSocket(socket);
    }

    /**
     * Attempts to establish a Connection with the given Socket
     * @param socket the Socket to connect to
     * @return a Connection around the given Socket
     * @throws IOException if any exceptions occur when establishing the connection
     */
    public static Connection forSocket(Socket socket) throws IOException {
        return new Connection(socket, socket.getOutputStream(), socket.getInputStream());
    }
    
    public final void writeServerMessage(Message sm) throws IOException{        
        // do I need to make sure the JsonString contains no newlines?
        toClient.write(sm.toJsonString());
        toClient.write('\n');
        toClient.flush();
    }
    
    // blocks until the client writes something
    public final Message readServerMessage() throws IOException{
        // This works, so I guess toJsonString excapes newlines or something?
        Message deser = Message.deserializeJson(fromClient.readLine());
        
        return deser;
    }
    
    public void close(){
        try {
            toClient.close();
        } catch (IOException ex) {
            System.err.println("couldn't close output");
            ex.printStackTrace();
        }
        try {
            fromClient.close();
        } catch (IOException ex) {
            System.err.println("couldn't close input");
            ex.printStackTrace();
        }
        try {
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println("couldn't close socket");
            ex.printStackTrace();
        }
    }
    
    public void setRemoteUser(User u){
        remoteUser = u;
    }
    public User getRemoteUser(){
        return remoteUser;
    }
    
    @Override
    public String toString(){
        String userName = (remoteUser == null) ? "---" : remoteUser.getName();
        return String.format("Connection to %s:%d %s", 
            clientSocket.getInetAddress().getHostAddress(),
            clientSocket.getPort(),
            userName
        );
    }
}
