package net;

import net.messages.ServerMessagePacket;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import net.messages.ServerMessage;
import users.AbstractUser;

/**
 *
 * @author Matt
 */
public class Connection {
    private AbstractUser user; //the user playing Orpheus on the machine this connects to
    private final InetAddress receivingIp;
    private final Socket clientSocket;    
    private final BufferedReader fromClient;
    private final BufferedWriter toClient;
    
    public Connection(Socket s) throws IOException{
        System.out.println(String.format("(Connection constructor) Creating Connection(Socket(%s)) in Thread %s", s.getInetAddress().getHostAddress(), Thread.currentThread().toString()));
        receivingIp = s.getInetAddress();
        clientSocket = s;
        toClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    
    public final boolean writeServerMessage(ServerMessage sm){
        boolean success = false;
        
        try {
            // need to make sure the JsonString contains no newlines
            toClient.write(sm.toJsonString());
            toClient.write('\n');
            toClient.flush();
            success = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return success;
    }
    
    // blocks until the client writes something
    public final ServerMessagePacket readServerMessage() throws IOException{
        ServerMessage deser = ServerMessage.deserializeJson(fromClient.readLine());
        
        return new ServerMessagePacket(receivingIp, deser);
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
    
    public void setUser(AbstractUser u){
        user = u;
    }
    public AbstractUser getUser(){
        return user;
    }
    
    public void displayData(){
        System.out.print(clientSocket.getInetAddress().getHostAddress() + ": ");
        System.out.println((user == null) ? "---" : user.getName());
    }
}
