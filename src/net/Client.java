/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

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
public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread t;
    
    public Client(String ipAddr, int port){
        t = new Thread(){
            @Override
            public void run(){
                try{
                    socket = new Socket(ipAddr, port);
                    System.out.println("connected");
                    in = new DataInputStream(System.in);
                    out = new DataOutputStream(socket.getOutputStream());
                    
                    String line = "";
                    while(!"done".equals(line)){
                        line = in.readUTF();
                        out.writeUTF(line);
                    }
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }
    
    public static void main(String[] args){
        new Client("127.0.0.1", 5000);
    }
}
