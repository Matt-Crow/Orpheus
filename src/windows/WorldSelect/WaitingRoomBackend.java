package windows.WorldSelect;

import battle.Battle;
import battle.Team;
import controllers.Master;
import controllers.User;
import controllers.World;
import customizables.Build;
import entities.TruePlayer;
import java.awt.Color;
import java.io.IOException;
import static java.lang.System.err;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import net.OrpheusServer;
import net.OrpheusServerState;
import net.ServerMessage;
import net.ServerMessageType;
import serialization.JsonUtil;

/**
 * This class provides all the server
 * functionality needed by WSWaitForPlayers,
 * as that class got a bit out of hand with
 * all the functionality in one file.
 * 
 * @author Matt Crow
 */
public class WaitingRoomBackend {
    private final WSWaitForPlayers host;
    private OrpheusServer server;
    
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
    
    private int teamSize;
    private boolean isHost;
    private boolean gameAboutToStart;
    
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
    
    
    
    public WaitingRoomBackend(WSWaitForPlayers page){
        host = page;
        server = null;
        isHost = false;
        teamSize = 0;
        gameAboutToStart = false;
        
        team1Proto = new HashMap<>();
        team2Proto = new HashMap<>();
        
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
    }
    
    /**
     * Checks to see if the local server 
     * has started yet
     * @return whether or not this has been connected to Master's OrpheusServer
     */
    public boolean serverIsStarted(){
        return server != null;
    }
    
    /**
     * Initializes the server as a host,
     * setting up the appropriate receivers.
     * This should only be called by one 
     * user in each game
     * 
     * @return whether or not the server is 
     * successfully started.
     * Note that this does return true if 
     * the server has already initialized.
     */
    public boolean initHostServer(){
        boolean success = true;
        if(Master.getServer() == null){
            try {
                Master.startServer();
            } catch (IOException ex) {
                ex.printStackTrace();
                success = false;
            }
        }
        //            need this to make sure this hasn't started yet
        if(success && server == null){
            server = Master.getServer();
            server.setState(OrpheusServerState.WAITING_ROOM);
            
            server.addReceiver(ServerMessageType.PLAYER_JOINED, receiveJoin);
            server.addReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
            isHost = true;
        }
        return success;
    }
    
    /**
     * Initializes the server as a client,
     * that is to say, not the host.
     * Then, sets up the appropriate receivers.
     * 
     * @return whether or not the server is 
     * ready after the method completes
     */
    public boolean initClientServer(){
        boolean success = true;
        if(Master.getServer() == null){
            try {
                Master.startServer();
            } catch (IOException ex) {
                ex.printStackTrace();
                success = false;
            }
        }
        if(success && server == null){
            server = Master.getServer();
            server.setState(OrpheusServerState.WAITING_ROOM);
            
            server.addReceiver(ServerMessageType.WAITING_ROOM_INIT, receiveInit);
            server.addReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
            server.addReceiver(ServerMessageType.REQUEST_PLAYER_DATA, receivePlayerRequest);
            isHost = false;
        }
        return success;
    }
    
    
    
    
    
    public void setTeamSize(int s){
        if(s <= 1){
            teamSize = s;
        }
    }
    
    public User[] getTeam1Proto(){
        return team1Proto.values().stream().toArray(size -> new User[size]);
    }
    public User[] getTeam2Proto(){
        return team2Proto.values().stream().toArray(size -> new User[size]);
    }
    
    /**
     * Sends a message containing the state of this waiting room.
     * Is sent whenever a player joins the server
     * @param ipAddr the ipAddress to end the init info to
     */
    public void sendInit(String ipAddr){
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

        setTeamSize(obj.getInt("team size"));
        obj.getJsonArray("team 1").stream().forEach((jv)->{
            if(jv.getValueType().equals(JsonValue.ValueType.OBJECT)){
                tryJoinTeam1(User.deserializeJson((JsonObject)jv));
            }
        });
        obj.getJsonArray("team 2").stream().forEach((jv)->{
            if(jv.getValueType().equals(JsonValue.ValueType.OBJECT)){
                tryJoinTeam2(User.deserializeJson((JsonObject)jv));
            }
        });
        
        Master.getServer().removeReceiver(ServerMessageType.WAITING_ROOM_INIT, receiveInit);
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
                tryJoinTeam1(sm.getSender());
                break;
            case "2":
                tryJoinTeam2(sm.getSender());
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
                host.getSelectedBuild().serializeJson().toString(),
                ServerMessageType.PLAYER_DATA
            ),
            sm.getSender().getIpAddress()
        );
        host.setInputEnabled(false);
        
        Master.getServer().addReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
        Master.getServer().addReceiver(ServerMessageType.REQUEST_PLAYER_DATA, receivePlayerRequest);
        
        Master.getServer().addReceiver(ServerMessageType.NOTIFY_IDS, receiveRemoteIds);
        Master.getServer().addReceiver(ServerMessageType.WORLD_INIT, receiveWorldInit);
    }
    
    
    
    private void sendIds(String ipAddr, int teamId, int playerId){
        ServerMessage sm = new ServerMessage(
            String.format("team: %d, player: %d", teamId, playerId),
            ServerMessageType.NOTIFY_IDS
        );
        Master.getServer().send(sm, ipAddr);
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

        out.println(sm.getBody());
        if(team1Proto.containsKey(ip)){
            tp = team1Proto.get(ip).initPlayer().getPlayer();
            teamNum = 1;
            team1Proto.remove(ip);
        } else if(team2Proto.containsKey(ip)){
            tp = team2Proto.get(ip).initPlayer().getPlayer();
            teamNum = 2;
            team2Proto.remove(ip);
        } else {
            err.println("Ugh oh, " + sm.getSender().getName() + " isn't on any team!");
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
            out.println("team 1 is done");
        }
        if(team2.getRosterSize() == teamSize){
            out.println("team 2 is done");
        }
        if(team1.getRosterSize() == teamSize && team2.getRosterSize() == teamSize){
            Master.getServer().removeReceiver(ServerMessageType.PLAYER_DATA, receivePlayerBuild);
            finallyStart();
        }
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
    
    private void receiveWorldInit(ServerMessage sm){
        World w = World.fromSerializedString(sm.getBody());
        User me = Master.getUser(); //need to set player before calling createCanvas
        me.setPlayer((TruePlayer)w.getTeamById(me.getRemoteTeamId()).getMemberById(me.getRemotePlayerId()));
        w.createCanvas();
        w.init();
        
        //can change this to switchToPage once world canvas is a Page
        JFrame parent = (JFrame)SwingUtilities.getWindowAncestor(host);
        parent.setContentPane(w.getCanvas());
        parent.revalidate();
        w.getCanvas().requestFocus();
    }
    
    
    
    
    
    /**
     * Begins preparing the server to receive
     * player build information.
     * After 30 seconds, waits for data before starting the world
     */
    public void startWorld(){
        gameAboutToStart = true;
        OrpheusServer server = Master.getServer();
        host.setStartButtonEnabled(false);
        
        server.setAcceptingConn(false);
        server.removeReceiver(ServerMessageType.PLAYER_JOINED, receiveJoin);
        Timer t = new Timer(30000, (e)->{
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
        //need these here, else null pointer in responding to build request
        team1 = Team.constructRandomTeam("Team 1", Color.green, teamSize - team1Proto.size());
        team2 = Team.constructRandomTeam("team 2", Color.red, teamSize - team2Proto.size());
        
        server.addReceiver(ServerMessageType.PLAYER_DATA, receivePlayerBuild);
        requestBuilds();
        
        host.setInputEnabled(false);
        
        //first, put the host on the proper team
        Master.getUser().initPlayer().getPlayer().applyBuild(host.getSelectedBuild());
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
            out.println("team 1 is done");
        }
        if(team2.getRosterSize() == teamSize){
            out.println("team 2 is done");
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
        JFrame parent = (JFrame)SwingUtilities.getWindowAncestor(host);
        parent.setContentPane(w.getCanvas());
        parent.revalidate();
        w.getCanvas().requestFocus();
    }
    
    
    public boolean isAlreadyStarted(){
        return gameAboutToStart;
    }
    
    public boolean team1Full(){
        return team1Proto.size() < teamSize;
    }
    public boolean team2Full(){
        return team2Proto.size() < teamSize;
    }
    
    //sending waiting room updates is done in these next two methods
    public boolean tryJoinTeam1(User u){
        boolean success = false;
        if(team1Full() && !team1Proto.containsKey(u.getIpAddress())){
            //team is not full, and u is not already on this team
            
            success = true;
            if(team2Proto.containsKey(u.getIpAddress())){
                team2Proto.remove(u.getIpAddress());
            }
            team1Proto.put(u.getIpAddress(), u);
            
            if(u.equals(Master.getUser())){
                //only send an update if the user is the one who changed teams. Prevents infinite loop
                ServerMessage sm = new ServerMessage(
                    "join team 1",
                    ServerMessageType.WAITING_ROOM_UPDATE
                );
                Master.getServer().send(sm);
            }
            //displayData();
        }
        return success;
    }
    public boolean tryJoinTeam2(User u){
        boolean success = false;
        if(team2Full() && !team2Proto.containsKey(u.getIpAddress())){
            //team is not full, and u is not already on this team
            
            success = true;
            if(team1Proto.containsKey(u.getIpAddress())){
                team1Proto.remove(u.getIpAddress());
            }
            team2Proto.put(u.getIpAddress(), u);
            
            if(u.equals(Master.getUser())){
                //only send an update if the user is the one who changed teams. Prevents infinite loop
                ServerMessage sm = new ServerMessage(
                    "join team 2",
                    ServerMessageType.WAITING_ROOM_UPDATE
                );
                Master.getServer().send(sm);
            }
            //displayData();
        }
        return success;
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
