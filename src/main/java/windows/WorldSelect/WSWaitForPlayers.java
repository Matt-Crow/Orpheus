package windows.WorldSelect;

import controllers.Master;
import controllers.User;
import customizables.Build;
import gui.*;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.*;
import net.OrpheusServer;
import net.protocols.WaitingRoomHostProtocol;
import windows.Page;

/**
 * WSWaitForPlayers is used to provide a "waiting room"
 * where users can stay in while they are waiting for other
 * players to join.
 * 
 * @author Matt Crow
 */
public class WSWaitForPlayers extends Page{
    /*
    For now, I'm using IP address as the key, and the User as the value.
    I'm not sure if this will work, I think IP addresses are unique to each computer,
    but I'm not quite sure
    */
    
    private final JTextArea teamList;
    private final Chat chat;
    private final BuildSelect playerBuild;
    private final JButton joinTeamButton;
    private final JButton startButton;
    
    private final WaitingRoomBackend backend;
    
    public WSWaitForPlayers(){
        super();
        
        addBackButton(new WSMain());
        
        //grid layout was causing problems with chat.
        //since it couldn't fit in 1/4 of the JPanel, it compressed to just a thin line
        setLayout(new BorderLayout());
        
        backend = new WaitingRoomBackend(this);
        
        JPanel infoSection = new JPanel();
        infoSection.setLayout(new GridLayout(1, 3));
        
        teamList = new JTextArea("Player Team:");
        Style.applyStyling(teamList);
        infoSection.add(teamList);
        
        add(infoSection, BorderLayout.PAGE_START);
        
        
        joinTeamButton = new JButton("Join team");
        joinTeamButton.addActionListener((e)->{
            //joinPlayerTeam(Master.getUser());
        });
        Style.applyStyling(joinTeamButton);
        add(joinTeamButton, BorderLayout.LINE_START);
        
        JPanel center = new JPanel();
        center.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.anchor = GridBagConstraints.LINE_START;
        playerBuild = new BuildSelect();
        center.add(playerBuild, gbc.clone());
        
        gbc.anchor = GridBagConstraints.LINE_END;
        chat = new Chat();
        center.add(chat, gbc.clone());
        
        add(center, BorderLayout.CENTER);
        
        
        startButton = new JButton("Start the match");
        startButton.addActionListener((e)->{
            if(backend.isHost() && !backend.isAlreadyStarted()){
                //backend.prepareToStart();
                chat.log("The game will start in " + WaitingRoomBackend.WAIT_TIME + " seconds. Please select your build and team.");
            }else{
                chat.logLocal("only the host can start the world. You'll have to wait for them.");
                chat.log("Are we waiting on anyone?");
            }
        });
        add(startButton, BorderLayout.PAGE_END);
        
        revalidate();
        repaint();
    }
    
    /**
     * starts the backend server (if it isn't already
     * started) and connects it to the given ipAddress,
     * which should be the host of a game.
     * 
     * @param ipAddr the ipAddress to connect to
     * @return this
     * @throws java.io.IOException if it cannot connect to the IP address
     */
    public WSWaitForPlayers joinServer(String ipAddr) throws IOException{
        backend.initClientServer();
        
        OrpheusServer.getInstance().connect(ipAddr);
        chat.joinChat(ipAddr);
        chat.logLocal("Connected to host " + ipAddr);
        
        return this;
    }
    
    
    
    
    public Build getSelectedBuild(){
        return playerBuild.getSelectedBuild();
    }
    
    public void setStartButtonEnabled(boolean b){
        startButton.setEnabled(b);
    }
    
    public void setInputEnabled(boolean b){
        playerBuild.setEnabled(b);
        joinTeamButton.setEnabled(b);
    }
    
    public WSWaitForPlayers setNumWaves(int s){
        backend.setNumWaves(s);
        return this;
    }
    public WSWaitForPlayers setMaxEnemyLevel(int l){
        backend.setMaxEnemyLevel(l);
        return this;
    }
}
