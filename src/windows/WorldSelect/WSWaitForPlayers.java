package windows.WorldSelect;

import battle.Team;
import controllers.Master;
import controllers.User;
import customizables.Build;
import entities.TruePlayer;
import gui.BuildSelect;
import gui.Chat;
import gui.Style;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.*;
import javax.swing.JButton;
import javax.swing.Timer;
import net.OrpheusServer;
import net.OrpheusServerState;
import net.ServerMessage;
import net.ServerMessageType;
import serialization.JsonUtil;
import windows.Page;
import windows.SubPage;

/**
 * need to differentiate between host and other people, so that way only the host can start the match and generate the world
 * @author Matt
 */
public class WSWaitForPlayers extends SubPage{
    /*
    For now, I'm using IP address as the key, and the User as the value.
    I'm not sure if this will work, I think IP addresses are unique to each computer,
    but I'm not quite sure
    */
    
    private boolean isHost;
    
    private final HashMap<String, User> team1;
    private final HashMap<String, User> team2;
    private final Chat chat;
    private final BuildSelect playerBuild;
    private final JButton joinT1Button;
    private final JButton joinT2Button;
    private final JButton startButton;
    
    private int teamSize;
    
    private final Consumer<ServerMessage> receiveJoin;
    private final Consumer<ServerMessage> receiveInit;
    private final Consumer<ServerMessage> receiveUpdate;
    private final Consumer<ServerMessage> receivePlayerRequest;
    
    //todo add build select, start button, display teams
    public WSWaitForPlayers(Page p){
        super(p);
        
        isHost = false;
        teamSize = 1;
        team1 = new HashMap<>();
        team2 = new HashMap<>();
        
        playerBuild = new BuildSelect();
        add(playerBuild);
        
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
        
        startButton = new JButton("Start the match");
        startButton.addActionListener((e)->{
            if(isHost){
                startWorld();
            }else{
                chat.logLocal("only the host can start the world. You'll have to wait for them.");
            }
        });
        add(startButton);
        
        receiveJoin  = (sm)->{
            sendInit(sm.getSender().getIpAddress());
        };
        
        receiveInit = (sm)->{
            JsonReader read = Json.createReader(new StringReader(sm.getBody()));
            JsonObject obj = read.readObject();
            read.close();

            JsonUtil.verify(obj, "team size");
            JsonUtil.verify(obj, "team 1");
            JsonUtil.verify(obj, "team 2");

            teamSize = obj.getInt("team size");
            obj.getJsonArray("team 1").stream().forEach((jv)->{
                if(jv.getValueType().equals(JsonValue.ValueType.OBJECT)){
                    joinTeam1(User.deserializeJson((JsonObject)jv));
                }
            });
            obj.getJsonArray("team 2").stream().forEach((jv)->{
                if(jv.getValueType().equals(JsonValue.ValueType.OBJECT)){
                    joinTeam2(User.deserializeJson((JsonObject)jv));
                }
            });
        };
        
        receiveUpdate = (sm)->{
            //not sure I like this.
            //update messages are either 'join team 1', or 'join team 2'
            String[] split = sm.getBody().split(" ");
            if(null == split[split.length - 1]){
                System.out.println("not sure how to handle this: ");
                sm.displayData();
            } else switch (split[split.length - 1]) {
                case "1":
                    joinTeam1(sm.getSender());
                    break;
                case "2":
                    joinTeam2(sm.getSender());
                    break;
                default:
                    System.out.println("not sure how to handle this: ");
                    sm.displayData();
                    break;
            }
        };
        
        receivePlayerRequest = (sm)->{
            Master.getServer().send(
                new ServerMessage(
                    playerBuild.getSelectedBuild().serializeJson().toString(),
                    ServerMessageType.PLAYER_DATA
                ),
                sm.getSender().getIpAddress()
            );
            playerBuild.setEnabled(false);
            joinT1Button.setEnabled(false);
            joinT2Button.setEnabled(false);
        };
        
        //grid layout was causing problems with chat.
        //since it couldn't fit in 1/4 of the JPanel, it compressed to just a thin line
        setLayout(new FlowLayout());
        revalidate();
        repaint();
    }
    
    public WSWaitForPlayers startServer(){
        if(Master.getServer() == null){
            try {
                Master.startServer();
                Master.getServer().setState(OrpheusServerState.WAITING_ROOM);
                chat.openChatServer();
                chat.logLocal("Server started on host address " + Master.getServer().getIpAddr());
                Master.getServer().addReceiver(ServerMessageType.PLAYER_JOINED, receiveJoin);
                Master.getServer().addReceiver(ServerMessageType.WAITING_ROOM_INIT, receiveInit);
                Master.getServer().addReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
                isHost = true;
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
            //Master.getServer().addReceiver(ServerMessageType.PLAYER_JOINED, receiveJoin); I don't think we need this on more than 1 person
            Master.getServer().addReceiver(ServerMessageType.WAITING_ROOM_INIT, receiveInit);
            Master.getServer().addReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
            Master.getServer().addReceiver(ServerMessageType.REQUEST_PLAYER_DATA, receivePlayerRequest);
        }
        return this;
    }
    
    /**
     * Sends a message containing the state of this waiting room.
     * Is sent whenever a player joins the server
     */
    private void sendInit(String ipAddr){
        JsonObjectBuilder build = Json.createObjectBuilder();
        build.add("type", "waiting room init");
        build.add("team size", teamSize);
        
        JsonArrayBuilder t1 = Json.createArrayBuilder();
        team1.values().stream().forEach((User u)->{
            t1.add(u.serializeJson());
        });
        build.add("team 1", t1.build());
        
        JsonArrayBuilder t2 = Json.createArrayBuilder();
        team2.values().stream().forEach((User u)->{
            t2.add(u.serializeJson());
        });
        build.add("team 2", t2.build());
        
        ServerMessage initMsg = new ServerMessage(
            build.build().toString(),
            ServerMessageType.WAITING_ROOM_INIT
        );
        
        Master.getServer().send(initMsg, ipAddr);
    }
    
    public WSWaitForPlayers joinTeam1(User u){
        if(team1.containsKey(u.getIpAddress())){
            chat.logLocal(u.getName() + " is already on team 1.");
        }else if(team1.size() < teamSize){
            if(team2.containsKey(u.getIpAddress())){
                team2.remove(u.getIpAddress());
                chat.logLocal(u.getName() + " has left team 2.");
            }
            team1.put(u.getIpAddress(), u);
            chat.logLocal(u.getName() + " has joined team 1.");
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
                chat.logLocal(u.getName() + " has left team 1.");
            }
            team2.put(u.getIpAddress(), u);
            chat.logLocal(u.getName() + " has joined team 2.");
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
    
    private void requestBuilds(){
        Master.getServer().send(new ServerMessage(
            "please provide build information",
            ServerMessageType.PLAYER_DATA
        ));
    }
    
    private void startWorld(){
        OrpheusServer server = Master.getServer();
        startButton.setEnabled(false);
        chat.log("The game will start in 30 seconds. Please select your build and team.");
        server.setAcceptingConn(false);
        server.removeReceiver(ServerMessageType.PLAYER_JOINED, receiveJoin);
        server.removeReceiver(ServerMessageType.WAITING_ROOM_INIT, receiveInit);
        Timer t = new Timer(30000, (e)->{
            chat.log("*ding*");
            server.removeReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
            
            waitForData();
        });
        t.setRepeats(false);
        t.start();
    }
    
    private void waitForData(){
        requestBuilds();
        
        playerBuild.setEnabled(false);
        joinT1Button.setEnabled(false);
        joinT2Button.setEnabled(false);
        
        Team t1 = Team.constructRandomTeam("Team 1", Color.green, teamSize - team1.size());
        Team t2 = Team.constructRandomTeam("team 2", Color.red, teamSize - team2.size());
        
        Master.getUser().initPlayer().getPlayer().applyBuild(playerBuild.getSelectedBuild());
        t1.addMember(Master.getUser().getPlayer());
        
        Master.getServer().addReceiver(ServerMessageType.PLAYER_DATA, (sm)->{
            String ip = sm.getSender().getIpAddress();
            TruePlayer tp;
            Build b;
            int teamNum;
            
            chat.logLocal(sm.getBody());
            if(team1.containsKey(ip)){
                tp = team1.get(ip).initPlayer().getPlayer();
                teamNum = 1;
                team1.remove(ip);
            } else if(team2.containsKey(ip)){
                tp = team2.get(ip).initPlayer().getPlayer();
                teamNum = 2;
                team2.remove(ip);
            } else {
                chat.logLocal("Ugh oh, " + sm.getSender().getName() + " isn't on any team!");
                return;
            }
            
            b = Build.deserializeJson(JsonUtil.fromString(sm.getBody()));
            tp.applyBuild(b);
            if(teamNum == 1){
                t1.addMember(tp);
            }else{
                t2.addMember(tp);
            }
            t1.displayData();
            t2.displayData();
            
            if(t1.getRosterSize() == teamSize){
                chat.log("team 1 is done");
            }
            if(t2.getRosterSize() == teamSize){
                chat.log("team 2 is done");
            }
            /*
            start world
            serialize the world and send it to all connected users
            switch to that new world
            notify users that the world has started
            remove all of this' receivers from the server
            notify users what team they are on, and their player's ID
            */
        });
        t1.displayData();
        t2.displayData();

        if(t1.getRosterSize() == teamSize){
            chat.log("team 1 is done");
        }
        if(t2.getRosterSize() == teamSize){
            chat.log("team 2 is done");
        }
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
