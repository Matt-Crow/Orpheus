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
import javax.swing.JOptionPane;

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
                    /*
                    String line = "";
                    while(!"done".equals(line)){
                        line = JOptionPane.showInputDialog("say something:");
                        System.out.println("writing utf...");
                        out.writeUTF(line);
                        System.out.println("flush the buffer");
                        out.flush();
                        System.out.println("wrote " + line);
                    }
                    terminate();*/
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }
    
    public void send(String msg){
        try{
            out.writeUTF(msg);
            out.flush();
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public void terminate(){
        try{
            send("done");
            in.close();
            out.close();
            socket.close();
        } catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        new Client(JOptionPane.showInputDialog("Enter IP address to connect to: "), 5000);
    }
}
