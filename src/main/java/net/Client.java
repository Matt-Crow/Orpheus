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
import java.util.function.Consumer;
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
    private Consumer<String> receiver;
    
    public Client(String ipAddr, int port, Consumer<String> onReceive){
        receiver = onReceive;
        t = new Thread(){
            @Override
            public void run(){
                try{
                    socket = new Socket(ipAddr, port);
                    System.out.println("connected");
                    in = new DataInputStream(socket.getInputStream());
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
                    }*/
                    String line = "";
                    while(!"done".equals(line)){
                        try {
                            System.out.println("reading utf...");
                            line = in.readUTF();
                            receiver.accept(line);
                            //System.out.println(line);
                        } catch (IOException ex) {
                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    terminate();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        t.start();
    }
    
    public void receive(String msg){
        receiver.accept(msg);
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
        new Client(JOptionPane.showInputDialog("Enter IP address to connect to: "), 5000, (String s)->System.out.println(s));
    }
}
