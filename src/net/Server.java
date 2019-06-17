/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matt
 */
public class Server {
    private Socket socket;
    private ServerSocket server;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread t;
    
    public Server(int port){
        t = new Thread(){
            @Override
            public void run(){
                try{
                    server = new ServerSocket(port);
                    System.out.println("Server started, waiting for client...");

                    socket = server.accept();
                    System.out.println("accepted");

                    in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                    //do something with out?
                    
                    String line = "";
                    while(!"done".equals(line)){
                        try {
                            line = in.readUTF();
                            System.out.println(line);
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
               
                    in.close();
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }
    
    public static void main(String[] args){
        new Server(5000);
    }
}
