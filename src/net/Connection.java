package net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
    
    
    public Connection(Socket s) throws IOException{
        clientSocket = s;
        ip = new DataInputStream(new BufferedInputStream(s.getInputStream()));
        op = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));
    }
    
    public void writeToClient(String s) throws IOException{
        op.writeUTF(s);
        op.flush();
    }
    
    public String readFromClient() throws IOException{
        return ip.readUTF();
    }
    
    public void close(){
        try {
            op.close();
        } catch (IOException ex) {
            System.err.println("couldn't close output");
            ex.printStackTrace();
        }
        try {
            ip.close();
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
