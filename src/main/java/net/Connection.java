package net;

import java.io.IOException;
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
        clientSocket = s;
        objOut = new ObjectOutputStream(s.getOutputStream());
        // https://stackoverflow.com/questions/8186135/java-sockets-program-stops-at-socket-getinputstream-w-o-error
        objIn = new ObjectInputStream(s.getInputStream());
        System.out.println();
    }
    
    public synchronized boolean writeToClient(String s){
        boolean success = false;
        try{
            objOut.writeObject(s);
            objOut.flush();
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
