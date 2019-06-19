package windows.WorldSelect;

import controllers.Master;
import entities.Player;
import java.util.ArrayList;
import gui.Chat;
import gui.Style;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import net.OrpheusServerState;
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
    
    //todo add build select, start button, display teams
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
        
        joinT2Button = new JButton("Joint team 2");
        joinT2Button.addActionListener((e)->{
            joinTeam2(Master.TRUEPLAYER);
        });
        Style.applyStyling(joinT2Button);
        add(joinT2Button);
        
        Chat.addTo(this);
        setLayout(new GridLayout(2, 1));
    }
    
    //todo set receiver function
    public WSWaitForPlayers startServer(){
        if(Master.getServer() == null){
            try {
                Master.startServer();
                Master.getServer().setState(OrpheusServerState.WAITING_ROOM);
                Chat.openChatServer(); //this will override any receiver function I set. Not sure what I'll do about that
                Chat.logLocal("Server started on host address " + Master.getServer().getIpAddr());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return this;
    }
    
    public WSWaitForPlayers joinServer(String ipAddr){
        if(Master.getServer() == null){
            try {
                Master.startServer();
            } catch (IOException ex) {
                Logger.getLogger(WSWaitForPlayers.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(Master.getServer() != null){//successfully started
            Master.getServer().connect(ipAddr);
            Chat.joinChat(ipAddr);
            Master.getServer().send("yay I connected");
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
            Chat.logLocal(p.getName() + " is already on team 1.");
        }else if(team1Players.size() < teamSize){
            team1Players.add(p);
            Chat.log(p.getName() + " has joined team 1.");
            Chat.log(team1Players.toString());
        }else{
            Chat.logLocal(p.getName() + " cannot joint team 1: Team 1 is full.");
        } 
        return this;
    }
    
    public WSWaitForPlayers joinTeam2(Player p){
        if(team1Players.contains(p)){
            team1Players.remove(p);
            Chat.log(p.getName() + " has left team 1.");
        }
        if(team2Players.contains(p)){
            Chat.logLocal(p.getName() + " is already on team 2.");
        }else if(team2Players.size() < teamSize){
            team2Players.add(p);
            Chat.log(p.getName() + " has joined team 2.");
            Chat.log(team2Players.toString());
        }else{
            Chat.logLocal(p.getName() + " cannot joint team 2: Team 2 is full.");
        } 
        return this;
    }
}
