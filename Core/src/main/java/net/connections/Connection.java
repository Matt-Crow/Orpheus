package net.connections;

import orpheus.core.net.messages.Message;
import orpheus.core.users.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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
    
    public Connection(Socket s) throws IOException{
        clientSocket = s;
        toClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
    
    public final Socket getClientSocket(){
        return clientSocket;
    }
    
    public void displayData(){
        System.out.print(clientSocket.getInetAddress().getHostAddress() + ": ");
        System.out.println((remoteUser == null) ? "---" : remoteUser.getName());
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
