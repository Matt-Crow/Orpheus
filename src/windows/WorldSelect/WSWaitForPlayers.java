package windows.WorldSelect;

import controllers.Master;
import controllers.User;
import java.util.ArrayList;
import gui.Chat;
import gui.Style;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import net.OrpheusServerState;
import net.ServerMessage;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class WSWaitForPlayers extends SubPage{
    private int teamSize;
    private final ArrayList<User> team1;
    private final ArrayList<User> team2;
    private final Chat chat;
    private final JButton joinT1Button;
    private final JButton joinT2Button;
    
    //todo add build select, start button, display teams
    public WSWaitForPlayers(Page p){
        super(p);
        
        teamSize = 1;
        team1 = new ArrayList<>();
        team2 = new ArrayList<>();
        
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
            Master.getServer().setReceiverFunction(ServerMessage.WAITING_ROOM_UPDATE, (ServerMessage sm)->{
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
        if(team1.contains(u)){
            chat.logLocal(u.getName() + " is already on team 1.");
        }else if(team1.size() < teamSize){
            if(team2.contains(u)){
                team2.remove(u);
                chat.log(u.getName() + " has left team 2.");
            }
            team1.add(u);
            chat.log(u.getName() + " has joined team 1.");
            if(u.equals(Master.getUser())){
                //only send an update if the user is the one who changed teams. Prevents infinite loop
                ServerMessage sm = new ServerMessage(
                    "join team 1",
                    ServerMessage.WAITING_ROOM_UPDATE
                );
                Master.getServer().send(sm);
            }
        }else{
            chat.logLocal(u.getName() + " cannot joint team 1: Team 1 is full.");
        } 
        return this;
    }
    
    public WSWaitForPlayers joinTeam2(User u){
        if(team2.contains(u)){
            chat.logLocal(u.getName() + " is already on team 2.");
        }else if(team2.size() < teamSize){
            if(team1.contains(u)){
                team1.remove(u);
                chat.log(u.getName() + " has left team 1.");
            }
            team2.add(u);
            chat.log(u.getName() + " has joined team 2.");
            if(u.equals(Master.getUser())){
                //only send an update if the user is the one who changed teams. Prevents infinite loop
                ServerMessage sm = new ServerMessage(
                    "join team 2",
                    ServerMessage.WAITING_ROOM_UPDATE
                );
                Master.getServer().send(sm);
            }
        }else{
            chat.logLocal(u.getName() + " cannot joint team 2: Team 2 is full.");
        } 
        return this;
    }
    
    public WSWaitForPlayers setTeamSize(int s){
        teamSize = s;
        return this;
    }
}
