package windows.WorldSelect;

import controllers.Master;
import entities.Player;
import java.util.ArrayList;
import gui.Chat;
import gui.Style;
import java.awt.GridLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class WSWaitForPlayers extends SubPage{
    private int teamSize;
    private ArrayList<Player> team1Players;
    private ArrayList<Player> team2Players;
    private JButton joinT1Button;
    private JButton joinT2Button;
    private Thread serverListenerThread;
    private Socket socket;
    
    public WSWaitForPlayers(Page p){
        super(p);
        
        teamSize = 1;
        team1Players = new ArrayList<>();
        team2Players = new ArrayList<>();
        
        joinT1Button = new JButton("Joint team 1");
        joinT1Button.addActionListener((e)->{
            joinTeam1(Master.TRUEPLAYER);
        });
        Style.applyStyling(joinT1Button);
        add(joinT1Button);
        
        Chat.addTo(this);
        setLayout(new GridLayout(2, 1));
    }
    
    public WSWaitForPlayers startServer(){
        if(serverListenerThread == null){
            serverListenerThread = new Thread(){
                @Override
                public void run(){
                    int port = 6066;
                    String server = "localhost";
                    try {
                        ServerSocket servSock = new ServerSocket(port);
                        socket = servSock.accept();
                        new DataOutputStream(socket.getOutputStream()).writeUTF("Hello, is this thing on?");
                        while(!socket.isInputShutdown()){
                            System.out.println(new DataInputStream(socket.getInputStream()).readUTF());
                        }
                        socket.close();
                    } catch (IOException ex) {
                        Chat.log(ex.getMessage());
                    }
                }
            };
            serverListenerThread.start();
        }
        
        return this;
    }
    
    public WSWaitForPlayers setTeamSize(int s){
        teamSize = s;
        return this;
    }
    
    public WSWaitForPlayers joinTeam1(Player p){
        if(team2Players.contains(p)){
            team2Players.remove(p);
            Chat.log(p.getName() + " has left team 2.");
        }
        if(team1Players.contains(p)){
            Chat.log(p.getName() + " is already on team 1.");
        }else if(team1Players.size() < teamSize){
            team1Players.add(p);
            Chat.log(p.getName() + " has joined team 1.");
            if(socket != null){
                try {
                    new ObjectOutputStream(socket.getOutputStream()).writeObject(team1Players);
                } catch (IOException ex) {
                    Logger.getLogger(WSWaitForPlayers.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            Chat.log(p.getName() + " cannot joint team 1: Team 1 is full.");
        } 
        return this;
    }
    
    public WSWaitForPlayers joinTeam2(Player p){
        if(team1Players.contains(p)){
            team1Players.remove(p);
            Chat.log(p.getName() + " has left team 1.");
        }
        if(team2Players.contains(p)){
            Chat.log(p.getName() + " is already on team 2.");
        }else if(team2Players.size() < teamSize){
            team2Players.add(p);
            Chat.log(p.getName() + " has joined team 2.");
            if(socket != null){
                try {
                    new ObjectOutputStream(socket.getOutputStream()).writeObject(team2Players);
                } catch (IOException ex) {
                    Logger.getLogger(WSWaitForPlayers.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }else{
            Chat.log(p.getName() + " cannot joint team 2: Team 2 is full.");
        } 
        return this;
    }
}
