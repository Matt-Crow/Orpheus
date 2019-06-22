package windows.WorldSelect;

import controllers.Master;
import controllers.User;
import gui.Chat;
import gui.Style;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import net.OrpheusServerState;
import net.ServerMessage;
import net.ServerMessageType;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class WSWaitForPlayers extends SubPage{
    private int teamSize;
    
    /*
    For now, I'm using IP address as the key, and the User as the value.
    I'm not sure if this will work, I think IP addresses are unique to each computer,
    but I'm not quite sure
    */
    private final HashMap<String, User> team1;
    private final HashMap<String, User> team2;
    private final Chat chat;
    private final JButton joinT1Button;
    private final JButton joinT2Button;
    
    //todo add build select, start button, display teams
    public WSWaitForPlayers(Page p){
        super(p);
        
        teamSize = 1;
        team1 = new HashMap<>();
        team2 = new HashMap<>();
        
        joinT1Button = new JButton("Join team 1");
        joinT1Button.addActionListener((e)->{
            joinTeam1(Master.getUser());
        });
        Style.applyStyling(joinT1Button);
        add(joinT1Button);
        
        joinT2Button = new JButton("Join team 2");
        joinT2Button.addActionListener((e)->{
            joinTeam2(Master.getUser());
        });
        Style.applyStyling(joinT2Button);
        add(joinT2Button);
        
        chat = new Chat();
        add(chat);
        
        //grid layout was causing problems with chat.
        //since it couldn't fit in 1/4 of the JPanel, it compressed to just a thin line
        setLayout(new FlowLayout());
        revalidate();
        repaint();
    }
    
    //todo set receiver function
    public WSWaitForPlayers startServer(){
        if(Master.getServer() == null){
            try {
                Master.startServer();
                Master.getServer().setState(OrpheusServerState.WAITING_ROOM);
                chat.openChatServer();
                chat.logLocal("Server started on host address " + Master.getServer().getIpAddr());
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
            chat.joinChat(ipAddr);
            Master.getServer().setReceiverFunction(ServerMessageType.WAITING_ROOM_UPDATE, (ServerMessage sm)->{
                String[] split = sm.getBody().split(" ");
                if("1".equals(split[split.length - 1])){
                    joinTeam1(sm.getSender());
                } else if("1".equals(split[split.length - 1])){
                    joinTeam2(sm.getSender());
                } else {
                    chat.log("not sure how to handle this: ");
                    sm.displayData();
                }
            });
        }
        return this;
    }
    
    public WSWaitForPlayers joinTeam1(User u){
        if(team1.containsKey(u.getIpAddress())){
            chat.logLocal(u.getName() + " is already on team 1.");
        }else if(team1.size() < teamSize){
            if(team2.containsKey(u.getIpAddress())){
                team2.remove(u.getIpAddress());
                chat.log(u.getName() + " has left team 2.");
            }
            team1.put(u.getIpAddress(), u);
            chat.log(u.getName() + " has joined team 1.");
            if(u.equals(Master.getUser())){
                //only send an update if the user is the one who changed teams. Prevents infinite loop
                ServerMessage sm = new ServerMessage(
                    "join team 1",
                    ServerMessageType.WAITING_ROOM_UPDATE
                );
                Master.getServer().send(sm);
            }
            displayData();
        }else{
            chat.logLocal(u.getName() + " cannot joint team 1: Team 1 is full.");
        } 
        return this;
    }
    
    public WSWaitForPlayers joinTeam2(User u){
        if(team2.containsKey(u.getIpAddress())){
            chat.logLocal(u.getName() + " is already on team 2.");
        }else if(team2.size() < teamSize){
            if(team1.containsKey(u.getIpAddress())){
                team1.remove(u.getIpAddress());
                chat.log(u.getName() + " has left team 1.");
            }
            team2.put(u.getIpAddress(), u);
            chat.log(u.getName() + " has joined team 2.");
            if(u.equals(Master.getUser())){
                //only send an update if the user is the one who changed teams. Prevents infinite loop
                ServerMessage sm = new ServerMessage(
                    "join team 2",
                    ServerMessageType.WAITING_ROOM_UPDATE
                );
                Master.getServer().send(sm);
            }
            displayData();
        }else{
            chat.logLocal(u.getName() + " cannot joint team 2: Team 2 is full.");
        } 
        return this;
    }
    
    public WSWaitForPlayers setTeamSize(int s){
        teamSize = s;
        return this;
    }
    
    public void displayData(){
        System.out.println("WAITING ROOM");
        System.out.println("Team size: " + teamSize);
        System.out.println("Team 1: ");
        team1.values().forEach((member)->System.out.println("--" + member.getName()));
        System.out.println("Team 2: ");
        team2.values().forEach((member)->System.out.println("--" + member.getName()));
        System.out.println("END OF WAITING ROOM");
    }
}
