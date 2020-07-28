package net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import users.AbstractUser;

/**
 *
 * @author Matt
 */
public class Connection {
    private AbstractUser user; //the user playing Orpheus on the machine this connects to
    private final Socket clientSocket;
    //private final ObjectOutputStream objOut;
    //private final ObjectInputStream objIn;
    
    private final BufferedReader fromClient;
    private final BufferedWriter toClient;
    
    public Connection(Socket s) throws IOException{
        System.out.println(String.format("(Connection constructor) Creating Connection(Socket(%s)) in Thread %s", s.getInetAddress().getHostAddress(), Thread.currentThread().toString()));
        clientSocket = s;
        
        // new stuff
        toClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        /*
        Note: Must always flush output stream before opening
        input stream.
        Note: new ObjectInputStream(...) will hang if
        the connecting computer has not opened a Connection
        to this one
        */
        /*
        objOut = new ObjectOutputStream(s.getOutputStream());
        objOut.flush();
        // https://stackoverflow.com/questions/8186135/java-sockets-program-stops-at-socket-getinputstream-w-o-error
        // https://stackoverflow.com/questions/22515654/objectinputstream-hanging
        objIn = new ObjectInputStream(s.getInputStream());
        */
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
    
    /*
    public synchronized boolean writeToClient(String s){
        boolean success = false;
        try{
            objOut.writeObject(s);
            objOut.flush();
            
            // https://stackoverflow.com/questions/7495155/java-out-of-heap-space-during-serialization
            
            //So, apparently, ObjectOutputStreams cache EVERYTHING they write out, leading to an eventual
            //OutOfMemoryError. To prevent this from happening, I must user the reset() method to force the
            //stream to un-cache all of these objects it's sent, allowing garbage collection to eat them.
            
            objOut.reset();
            
            success = true;
        } catch(IOException ex){
            ex.printStackTrace();
            System.err.println("Failed to write this to client: ");
            System.err.println(s);
        } catch(ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
            System.err.println("Too big?");
            System.err.println(s);
        }
        if(!success){
            try {
                objOut.reset();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return success;
    }
    
    public String readFromClient() throws IOException, ClassNotFoundException{
        return (String)objIn.readObject();
    }
    */
    
    public final ServerMessage readServerMessage() throws IOException{
        return ServerMessage.deserializeJson(fromClient.readLine());
    }
    
    public void close(){
        try {
            toClient.close();
            //objOut.close();
        } catch (IOException ex) {
            System.err.println("couldn't close output");
            ex.printStackTrace();
        }
        try {
            //objIn.close();
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
