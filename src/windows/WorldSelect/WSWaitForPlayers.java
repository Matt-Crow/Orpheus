package windows.WorldSelect;

import battle.Battle;
import battle.Team;
import controllers.Master;
import controllers.User;
import controllers.World;
import customizables.Build;
import entities.TruePlayer;
import gui.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import net.*;
import serialization.JsonUtil;
import windows.Page;
import windows.SubPage;

/**
 * WSWaitForPlayers is used to provide a "waiting room"
 * where users can stay in while they are waiting for other
 * players to join.
 * 
 * This class also handles generating and starting the World
 * where the Battle will take place.
 * 
 * Currently, this uses a peer-to-peer network, but with the 
 * user who opened this waiting room being designated as the "host".
 * The host basically acts as a regular user in the network, but will 
 * be in charge of updating the World. While this does give them the 
 * advantage of not having to deal with latency, I sincerely doubt 
 * Orpheus will become an e-sport any time soon.
 * 
 * There is a LOT of stuff here, so I may want to split this into
 * the graphic components in this class, and all the server stuff
 * in another
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
    
    private int teamSize;
    private boolean isHost;
    
    /*
    these prototype teams allow this to keep track of
    which user wants to be on each team.
    The key is the user's IP address, while the value
    is a local copy of that user's remote User object.
    */
    private final HashMap<String, User> team1Proto;
    private final HashMap<String, User> team2Proto;
    
    
    /*
    these are the real teams, constucted once the host
    sends the request for Build data
    */
    private Team team1;
    private Team team2;
    
    //applied only to the host. Notifies when a user initially joins
    private final Consumer<ServerMessage> receiveJoin;
    
    //not applied to the host. After a user initially joins, 
    //the host must notify them of the current state of the waiting room.
    //this allows the user to do that
    private final Consumer<ServerMessage> receiveInit;
    
    //applies to all users. Notifies that a player has changed teams
    private final Consumer<ServerMessage> receiveUpdate;
    
    //not applied to the host. Notifies the user that the host needs their Build information
    //also tells the user that the game is about to start, so it shuts down most of their receivers
    private final Consumer<ServerMessage> receivePlayerRequest;
    
    private final Consumer<ServerMessage> receivePlayerBuild;
    
    /*
    Since the Player this user is going to control is stored
    on a remote computer, they need some way of knowing what
    their team and player IDs are on that other computer
    */
    private final Consumer<ServerMessage> receiveRemoteIds;
    
    /*
    allows remote users to receive and deserialize the World
    created by the host
    */
    private final Consumer<ServerMessage> receiveWorldInit;
    
    public WSWaitForPlayers(Page p){
        super(p);
        
        //grid layout was causing problems with chat.
        //since it couldn't fit in 1/4 of the JPanel, it compressed to just a thin line
        setLayout(new BorderLayout());
        
        isHost = false;
        teamSize = 1;
        team1Proto = new HashMap<>();
        team2Proto = new HashMap<>();
        
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
        
        playerBuild = new BuildSelect();
        add(playerBuild, BorderLayout.PAGE_END);
        
        chat = new Chat();
        add(chat, BorderLayout.CENTER);
        
        startButton = new JButton("Start the match");
        startButton.addActionListener((e)->{
            if(isHost){
                startWorld();
            }else{
                chat.logLocal("only the host can start the world. You'll have to wait for them.");
                chat.log("Are we waiting on anyone?");
            }
        });
        add(startButton, BorderLayout.PAGE_END);
        
        receiveJoin  = (sm)->{
            sendInit(sm.getSender().getIpAddress());
        };
        
        receiveInit = (sm)->{
            receiveInit(sm);
        };
        
        receiveUpdate = (sm)->{
            receiveUpdate(sm);
        };
        
        receiveRemoteIds = (sm)->{
            receiveRemoteIds(sm);
        };
        
        receivePlayerRequest = (sm)->{
            receivePlayerRequest(sm);
        };
        
        receivePlayerBuild = (sm)->{
            receiveBuildInfo(sm);
        };
        
        receiveWorldInit = (sm)->{
            receiveWorldInit(sm);
        };
        
        revalidate();
        repaint();
    }
    
    public WSWaitForPlayers startServer(){
        if(Master.getServer() == null){
            try {
                Master.startServer();
                OrpheusServer server = Master.getServer();
                
                server.setState(OrpheusServerState.WAITING_ROOM);
                chat.openChatServer();
                chat.logLocal("Server started on host address " + Master.getServer().getIpAddr());
                server.addReceiver(ServerMessageType.PLAYER_JOINED, receiveJoin);
                //server.addReceiver(ServerMessageType.WAITING_ROOM_INIT, receiveInit);
                server.addReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
                isHost = true;
            } catch (IOException ex) {
                ex.printStackTrace();
                chat.logLocal("Unable to start server :(");
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
        team1Proto.values().stream().forEach((User u)->{
            t1.add(u.serializeJson());
        });
        build.add("team 1", t1.build());
        
        JsonArrayBuilder t2 = Json.createArrayBuilder();
        team2Proto.values().stream().forEach((User u)->{
            t2.add(u.serializeJson());
        });
        build.add("team 2", t2.build());
        
        ServerMessage initMsg = new ServerMessage(
            build.build().toString(),
            ServerMessageType.WAITING_ROOM_INIT
        );
        
        Master.getServer().send(initMsg, ipAddr);
    }
    private void receiveInit(ServerMessage sm){
        JsonObject obj = JsonUtil.fromString(sm.getBody());
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
        
        Master.getServer().removeReceiver(ServerMessageType.WAITING_ROOM_INIT, receiveInit);
    }
    
    //sending waiting room updates is done in these next two methods
    public WSWaitForPlayers joinTeam1(User u){
        if(team1Proto.containsKey(u.getIpAddress())){
            chat.logLocal(u.getName() + " is already on team 1.");
        }else if(team1Proto.size() < teamSize){
            if(team2Proto.containsKey(u.getIpAddress())){
                team2Proto.remove(u.getIpAddress());
            }
            team1Proto.put(u.getIpAddress(), u);
            chat.logLocal(u.getName() + " has joined team 1.");
            
            updateTeamDisplays();
            
            if(u.equals(Master.getUser())){
                //only send an update if the user is the one who changed teams. Prevents infinite loop
                yourTeam.setText("You are on team 1");
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
        if(team2Proto.containsKey(u.getIpAddress())){
            chat.logLocal(u.getName() + " is already on team 2.");
        }else if(team2Proto.size() < teamSize){
            if(team1Proto.containsKey(u.getIpAddress())){
                team1Proto.remove(u.getIpAddress());
            }
            team2Proto.put(u.getIpAddress(), u);
            chat.logLocal(u.getName() + " has joined team 2.");
            
            updateTeamDisplays();
            
            if(u.equals(Master.getUser())){
                //only send an update if the user is the one who changed teams. Prevents infinite loop
                yourTeam.setText("You are on team 2");
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
    private void updateTeamDisplays(){
        String newStr = "Team 1: \n";
        newStr = team1Proto
            .values()
            .stream()
            .map((User use) -> "* " + use.getName() + "\n")
            .reduce(newStr, String::concat);
        team1List.setText(newStr);
        
        newStr = "Team 2: \n";
        newStr = team2Proto
            .values()
            .stream()
            .map((User use) -> "* " + use.getName() + "\n")
            .reduce(newStr, String::concat);
        team2List.setText(newStr);
    }
    private void receiveUpdate(ServerMessage sm){
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
    }
    
    
    private void requestBuilds(){
        Master.getServer().send(new ServerMessage(
            "please provide build information",
            ServerMessageType.REQUEST_PLAYER_DATA
        ));
    }
    
    //called after the host requests this user's Build data.
    //the game is about to start, so shut down most receivers
    //also, prepare to receive IDs and World info from the host.
    private void receivePlayerRequest(ServerMessage sm){
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
        Master.getServer().addReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
        Master.getServer().addReceiver(ServerMessageType.REQUEST_PLAYER_DATA, receivePlayerRequest);
        
        Master.getServer().addReceiver(ServerMessageType.NOTIFY_IDS, receiveRemoteIds);
        Master.getServer().addReceiver(ServerMessageType.WORLD_INIT, receiveWorldInit);
    }
    
    /**
     * called after the host receives a user's Build
     * information.
     * 
     * applies the Build to a TruePlayer which will be controlled
     * by the message's sender. After constructing the new player,
     * adds them to the appropriate team, then notifies the message's
     * sender of what their team and player IDs are on this remote computer
     * 
     * @param sm a server message containing the sender's Build, serialized as a JSON object string
     */
    private void receiveBuildInfo(ServerMessage sm){
        String ip = sm.getSender().getIpAddress();
        TruePlayer tp;
        Build b;
        int teamNum;

        chat.logLocal(sm.getBody());
        if(team1Proto.containsKey(ip)){
            tp = team1Proto.get(ip).initPlayer().getPlayer();
            teamNum = 1;
            team1Proto.remove(ip);
        } else if(team2Proto.containsKey(ip)){
            tp = team2Proto.get(ip).initPlayer().getPlayer();
            teamNum = 2;
            team2Proto.remove(ip);
        } else {
            chat.logLocal("Ugh oh, " + sm.getSender().getName() + " isn't on any team!");
            return;
        }

        b = Build.deserializeJson(JsonUtil.fromString(sm.getBody()));
        tp.applyBuild(b);
        if(teamNum == 1){
            team1.addMember(tp);
        }else{
            team2.addMember(tp);
        }
        sendIds(ip, (teamNum == 1) ? team1.getId() : team2.getId(), tp.id);
        
        team1.displayData();
        team2.displayData();

        if(team1.getRosterSize() == teamSize){
            chat.log("team 1 is done");
        }
        if(team2.getRosterSize() == teamSize){
            chat.log("team 2 is done");
        }
        if(team1.getRosterSize() == teamSize && team2.getRosterSize() == teamSize){
            Master.getServer().removeReceiver(ServerMessageType.PLAYER_DATA, receivePlayerBuild);
            finallyStart();
        }
    }
    
    private void sendIds(String ipAddr, int teamId, int playerId){
        ServerMessage sm = new ServerMessage(
            String.format("team: %d, player: %d", teamId, playerId),
            ServerMessageType.NOTIFY_IDS
        );
        Master.getServer().send(sm, ipAddr);
    }
    
    
    
    
    
    /*
    Since the Player this user is going to be controlling
    is on another computer, this needs some way of knowing
    the IDs of the team and player this user is controlling
    on that computer.
    */
    private void receiveRemoteIds(ServerMessage sm){
        String[] split = sm.getBody().split(",");
        int tId = Integer.parseInt(split[0].replace("team:", "").trim());
        int pId = Integer.parseInt(split[1].replace("player:", "").trim());
        System.out.printf("OK, so my team's ID is %d, and my player ID is %d, right?", tId, pId);
        
        Master.getUser().setRemoteTeamId(tId).setRemotePlayerId(pId);
        
        Master.getServer().removeReceiver(ServerMessageType.NOTIFY_IDS, receiveRemoteIds);
    }
    
    /**
     * Serializes the world, and sends it
     * to each connected user, excluding the host
     * @param w 
     */
    private void sendWorldInit(World w){
        ServerMessage sm = new ServerMessage(
            w.serializeToString(),
            ServerMessageType.WORLD_INIT
        );
        Master.getServer().send(sm);
    }
    
    //not done, still need to set User's player to the one denoted by receiveIds
    private void receiveWorldInit(ServerMessage sm){
        World w = World.fromSerializedString(sm.getBody());
        User me = Master.getUser(); //need to set player before calling createCanvas
        me.setPlayer((TruePlayer)w.getTeamById(me.getRemoteTeamId()).getMemberById(me.getRemotePlayerId()));
        w.createCanvas();
        w.init();
        
        
        
        //can change this to switchToPage once world canvas is a Page
        JFrame parent = (JFrame)SwingUtilities.getWindowAncestor(this);
        parent.setContentPane(w.getCanvas());
        parent.revalidate();
        w.getCanvas().requestFocus();
        
        System.out.println("received " + sm.getBody());
    }
    
    
    
    
    
    /**
     * Begins preparing the server to receive
     * player build information.
     * After 30 seconds, waits for data before starting the world
     */
    private void startWorld(){
        OrpheusServer server = Master.getServer();
        startButton.setEnabled(false);
        chat.log("The game will start in 30 seconds. Please select your build and team.");
        server.setAcceptingConn(false);
        server.removeReceiver(ServerMessageType.PLAYER_JOINED, receiveJoin);
        Timer t = new Timer(30000, (e)->{
            chat.log("*ding*");
            server.removeReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
            waitForData();
        });
        t.setRepeats(false);
        t.start();
    }
    
    /**
     * Begins constructing teams, and sends a request for user Build data.
     * Note that receiveBuildInfo calls finallyStart, not this method.
     * 
     * @see WSWaitForPlayers#receiveBuildInfo
     */
    private void waitForData(){
        OrpheusServer server = Master.getServer();
        server.addReceiver(ServerMessageType.PLAYER_DATA, receivePlayerBuild);
        requestBuilds();
        
        playerBuild.setEnabled(false);
        joinT1Button.setEnabled(false);
        joinT2Button.setEnabled(false);
        
        team1 = Team.constructRandomTeam("Team 1", Color.green, teamSize - team1Proto.size());
        team2 = Team.constructRandomTeam("team 2", Color.red, teamSize - team2Proto.size());
        
        //first, put the host on the proper team
        Master.getUser().initPlayer().getPlayer().applyBuild(playerBuild.getSelectedBuild());
        if(team1Proto.containsKey(server.getIpAddr())){
            team1.addMember(Master.getUser().getPlayer());
            team1Proto.remove(server.getIpAddr());
        } else if (team2Proto.containsKey(server.getIpAddr())){
            team2.addMember(Master.getUser().getPlayer());
            team2Proto.remove(server.getIpAddr());
        }
        
        team1.displayData();
        team2.displayData();

        if(team1.getRosterSize() == teamSize){
            chat.log("team 1 is done");
        }
        if(team2.getRosterSize() == teamSize){
            chat.log("team 2 is done");
        }
        if(team1.getRosterSize() == teamSize && team2.getRosterSize() == teamSize){
            Master.getServer().removeReceiver(ServerMessageType.PLAYER_DATA, receivePlayerBuild);
            finallyStart();
        }
    }
    
    
    
    private void finallyStart(){
        World w = World.createDefaultBattle();
        Battle b = new Battle(
            w.getCanvas(),
            team1,
            team2
        );
        w.addTeam(team1).addTeam(team2).setCurrentMinigame(b);
        b.setHost(w);
        w.init();
        
        sendWorldInit(w);
        
        /*
        notify users that the world has started
        remove all of this' receivers from the server
        */
        
        //can change this to switchToPage once world canvas is a Page
        JFrame parent = (JFrame)SwingUtilities.getWindowAncestor(this);
        parent.setContentPane(w.getCanvas());
        parent.revalidate();
        w.getCanvas().requestFocus();
    }
    
    
    
    public WSWaitForPlayers setTeamSize(int s){
        teamSize = s;
        return this;
    }
    
    public void displayData(){
        System.out.println("WAITING ROOM");
        System.out.println("Team size: " + teamSize);
        System.out.println("Team 1: ");
        team1Proto.values().forEach((member)->System.out.println("--" + member.getName()));
        System.out.println("Team 2: ");
        team2Proto.values().forEach((member)->System.out.println("--" + member.getName()));
        System.out.println("END OF WAITING ROOM");
    }
}
