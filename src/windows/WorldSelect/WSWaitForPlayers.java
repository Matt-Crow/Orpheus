package windows.WorldSelect;

import controllers.Master;
import controllers.User;
import customizables.Build;
import gui.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import javax.swing.*;
import windows.Page;
import windows.SubPage;

/**
 * WSWaitForPlayers is used to provide a "waiting room"
 * where users can stay in while they are waiting for other
 * players to join.
 * 
 * @author Matt Crow
 */
public class WSWaitForPlayers extends SubPage{
    /*
    For now, I'm using IP address as the key, and the User as the value.
    I'm not sure if this will work, I think IP addresses are unique to each computer,
    but I'm not quite sure
    */
    
    private final JLabel yourTeam;
    private final JTextArea team1List;
    private final JTextArea team2List;
    private final Chat chat;
    private final BuildSelect playerBuild;
    private final JButton joinT1Button;
    private final JButton joinT2Button;
    private final JButton startButton;
    
    private final WaitingRoomBackend backend;
    
    public WSWaitForPlayers(Page p){
        super(p);
        
        //grid layout was causing problems with chat.
        //since it couldn't fit in 1/4 of the JPanel, it compressed to just a thin line
        setLayout(new BorderLayout());
        
        backend = new WaitingRoomBackend(this);
        
        JPanel infoSection = new JPanel();
        infoSection.setLayout(new GridLayout(1, 3));
        
        team1List = new JTextArea("Team 1");
        Style.applyStyling(team1List);
        infoSection.add(team1List);
        
        yourTeam = new JLabel("Your team");
        Style.applyStyling(yourTeam);
        infoSection.add(yourTeam);
        
        team2List = new JTextArea("Team 2");
        Style.applyStyling(team2List);
        infoSection.add(team2List);
        
        add(infoSection, BorderLayout.PAGE_START);
        
        
        joinT1Button = new JButton("Join team 1");
        joinT1Button.addActionListener((e)->{
            joinTeam1(Master.getUser());
        });
        Style.applyStyling(joinT1Button);
        add(joinT1Button, BorderLayout.LINE_START);
        
        joinT2Button = new JButton("Join team 2");
        joinT2Button.addActionListener((e)->{
            joinTeam2(Master.getUser());
        });
        Style.applyStyling(joinT2Button);
        add(joinT2Button, BorderLayout.LINE_END);
        
        JPanel center = new JPanel();
        center.setLayout(new FlowLayout());
        
        playerBuild = new BuildSelect();
        center.add(playerBuild);
        
        chat = new Chat();
        center.add(chat);
        
        add(center, BorderLayout.CENTER);
        
        
        startButton = new JButton("Start the match");
        startButton.addActionListener((e)->{
            if(backend.isHost() && !backend.isAlreadyStarted()){
                backend.prepareToStart();
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
     * starts the backend server (if it isn't
     * already started) as the host of a game.
     * 
     * @return this 
     */
    public WSWaitForPlayers startServer(){
        if(!backend.serverIsStarted()){
            boolean success = backend.initHostServer();
            if(success){
                chat.openChatServer();
                chat.logLocal("Server started on host address " + Master.getServer().getIpAddr());
            }else{
                chat.logLocal("Unable to start server :(");
            }
        }
        return this;
    }
    
    /**
     * starts the backend server (if it isn't already
     * started) and connects it to the given ipAddress,
     * which should be the host of a game.
     * 
     * @param ipAddr the ipAddress to connect to
     * @return this
     */
    public WSWaitForPlayers joinServer(String ipAddr){
        if(!backend.serverIsStarted()){
            boolean success = backend.initClientServer();
            if(success){
                Master.getServer().connect(ipAddr);
                chat.joinChat(ipAddr);
                chat.logLocal("Connected to host " + ipAddr);
            } else {
                chat.logLocal("Failed to connect to " + ipAddr);
            }
        }
        return this;
    }
    
    /**
     * Places a given User on team 1, if it isn't full.
     * If team 1 is full, but team 2 isn't, places them on team 2.
     * Note that if there are no open positions on either team,
     * that user will not be put on either team.
     * 
     * @param u the user to place on team 1.
     * @return this 
     */
    public WSWaitForPlayers joinTeam1(User u){
        if(backend.tryJoinTeam1(u)){
            chat.logLocal(u.getName() + " has joined team 1.");
            updateTeamDisplays();
            yourTeam.setText("You are on team 1");
        }else if(backend.team1Full() && backend.team2Full()){
            chat.logLocal(u.getName() + " cannot join either team: both teams are full");
        }else{
            chat.logLocal(u.getName() + " cannot join team 1: Team 1 is full, so they'll join team 2.");
            joinTeam2(u);
        }
        return this;
    }
    /**
     * Places a given User on team 2, if it isn't full.
     * If team 2 is full, but team 1 isn't, places them on team 1.
     * Note that if there are no open positions on either team,
     * that user will not be put on either team.
     * 
     * @param u the user to place on team 2.
     * @return this 
     */
    public WSWaitForPlayers joinTeam2(User u){
        if(backend.tryJoinTeam2(u)){
            chat.logLocal(u.getName() + " has joined team 2.");
            updateTeamDisplays();
            yourTeam.setText("You are on team 2");
        }else if(backend.team1Full() && backend.team2Full()){
            chat.logLocal(u.getName() + " cannot join either team: both teams are full");
        }else{
            chat.logLocal(u.getName() + " cannot join team 2: Team 2 is full, so they'll join team 1.");
            joinTeam1(u);
        }
        return this;
    }
    /**
     * Updates the text of this' team displays
     * to match the proto-teams of the backend.
     */
    public void updateTeamDisplays(){
        String newStr = "Team 1: \n";
        newStr = Arrays
            .stream(backend.getTeam1Proto())
            .map((User use) -> "* " + use.getName() + "\n")
            .reduce(newStr, String::concat);
        team1List.setText(newStr);
        
        newStr = "Team 2: \n";
        newStr = Arrays
            .stream(backend.getTeam2Proto())
            .map((User use) -> "* " + use.getName() + "\n")
            .reduce(newStr, String::concat);
        team2List.setText(newStr);
    }
    
    public Build getSelectedBuild(){
        return playerBuild.getSelectedBuild();
    }
    
    public void setStartButtonEnabled(boolean b){
        startButton.setEnabled(b);
    }
    
    public void setInputEnabled(boolean b){
        playerBuild.setEnabled(b);
        joinT1Button.setEnabled(b);
        joinT2Button.setEnabled(b);
    }
    
    public WSWaitForPlayers setTeamSize(int s){
        backend.setTeamSize(s);
        return this;
    }
}
