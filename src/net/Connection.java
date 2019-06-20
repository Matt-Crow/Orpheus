package net;

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
    private final Socket clientSocket;
    private final DataInputStream ip;
    private final DataOutputStream op;
    //not sure if I need both
    //private final ObjectOutputStream objOut;
    //private final ObjectInputStream objIn;
    
    public Connection(Socket s) throws IOException{
        clientSocket = s;
        ip = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        op = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
        //objIn = new ObjectInputStream(s.getInputStream());
        //objOut = new ObjectOutputStream(s.getOutputStream());
    }
    
    public void writeToClient(String s) throws IOException{
        op.writeUTF(s);
        op.flush();
    }
    /*
    public void writeToClient(Object o) throws IOException{
        objOut.writeObject(o);
    }*/
    
    public String readFromClient() throws IOException{
        return ip.readUTF();
    }
    /*
    public Object readObjectFromClient() throws IOException, ClassNotFoundException{
        return objIn.readObject();
    }*/
    
    public void close(){
        try {
            op.close();
            //objOut.close();
        } catch (IOException ex) {
            System.err.println("couldn't close output");
            ex.printStackTrace();
        }
        try {
            ip.close();
            //objIn.close();
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
}
