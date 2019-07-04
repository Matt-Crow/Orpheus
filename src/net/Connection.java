package net;

import controllers.User;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matt
 */
public class Connection {
    private User user; //the user playing Orpheus on the machine this connects to
    private final Socket clientSocket;
    //private final DataInputStream ip;
    //private final DataOutputStream op;
    //not sure if I need both
    private final ObjectOutputStream objOut;
    private final ObjectInputStream objIn;
    
    private static final String END_MSG = "END OF LINE.";
    
    public Connection(Socket s) throws IOException{
        clientSocket = s;
        //ip = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        //op = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
        objOut = new ObjectOutputStream(s.getOutputStream());
        System.out.println("done init out");
        objIn = new ObjectInputStream(s.getInputStream());
        System.out.println("done init in");
    }
    
    public void writeToClient(String s) throws IOException{
        objOut.writeObject(s);
        objOut.flush();
        /*
        int strBytes = s.length() * 2; //2 bytes per character
        //can only write up to 512 bytes at a time using writeUTF,
        //and 3 are reserved for marking the beginning and end of message
        
        if(strBytes > 512 - 3){
            for(int start = 0; start + 509 < strBytes; start += strBytes){
                op.writeUTF(s.substring(start / 2, (start + strBytes) / 2));
            }
        }
        op.writeUTF(s);
        op.flush();*/
    }
    /*
    public void writeToClient(Object o) throws IOException{
        objOut.writeObject(o);
    }*/
    
    public String readFromClient() throws IOException, ClassNotFoundException{
        //return ip.readUTF();
        return (String)objIn.readObject();
    }
    /*
    public Object readObjectFromClient() throws IOException, ClassNotFoundException{
        return objIn.readObject();
    }*/
    
    public void close(){
        try {
            //op.close();
            objOut.close();
        } catch (IOException ex) {
            System.err.println("couldn't close output");
            ex.printStackTrace();
        }
        try {
            //ip.close();
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
    
    public void setUser(User u){
        user = u;
    }
    public User getUser(){
        return user;
    }
}
