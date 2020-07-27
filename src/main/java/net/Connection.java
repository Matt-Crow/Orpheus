package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import users.AbstractUser;

/**
 *
 * @author Matt
 */
public class Connection {
    private AbstractUser user; //the user playing Orpheus on the machine this connects to
    private final Socket clientSocket;
    private final ObjectOutputStream objOut;
    private final ObjectInputStream objIn;
    
    public Connection(Socket s) throws IOException{
        System.out.println(String.format("(Connection constructor) Creating Connection(Socket(%s)) in Thread %s", s.getInetAddress().getHostAddress(), Thread.currentThread().toString()));
        clientSocket = s;
        /*
        Note: Must always flush output stream before opening
        input stream.
        Note: new ObjectInputStream(...) will hang if
        the connecting computer has not opened a Connection
        to this one
        */
        objOut = new ObjectOutputStream(s.getOutputStream());
        objOut.flush();
        // https://stackoverflow.com/questions/8186135/java-sockets-program-stops-at-socket-getinputstream-w-o-error
        // https://stackoverflow.com/questions/22515654/objectinputstream-hanging
        objIn = new ObjectInputStream(s.getInputStream());
    }
    
    public synchronized boolean writeToClient(String s){
        boolean success = false;
        try{
            objOut.writeObject(s);
            objOut.flush();
            
            // https://stackoverflow.com/questions/7495155/java-out-of-heap-space-during-serialization
            /*
            So, apparently, ObjectOutputStreams cache EVERYTHING they write out, leading to an eventual
            OutOfMemoryError. To prevent this from happening, I must user the reset() method to force the
            stream to un-cache all of these objects it's sent, allowing garbage collection to eat them.
            */
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
    
    public void close(){
        try {
            objOut.close();
        } catch (IOException ex) {
            System.err.println("couldn't close output");
            ex.printStackTrace();
        }
        try {
            objIn.close();
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
