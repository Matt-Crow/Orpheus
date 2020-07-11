package windows.WorldSelect;

import battle.Battle;
import battle.Team;
import controllers.*;
import controls.RemotePlayerControls;
import controls.SoloPlayerControls;
import customizables.Build;
import customizables.BuildJsonUtil;
import entities.HumanPlayer;
import java.awt.Color;
import java.io.IOException;
import static java.lang.System.err;
import java.util.HashMap;
import java.util.function.Consumer;
import javax.json.*;
import javax.swing.*;
import net.*;
import serialization.JsonUtil;
import windows.world.WorldCanvas;
import windows.world.WorldPage;
import world.HostWorld;
import world.RemoteProxyWorld;
import world.WorldContent;

/**
 * TODO: split this into multiple different protocols based on whether someone is a host or client.
 * 
 * This class provides all the server
 * functionality needed by WSWaitForPlayers,
 * as that class got a bit out of hand with
 * all the functionality in one file.
 * 
 * This class also handles generating and starting the AbstractWorld
 where the Battle will take place.
 
 Currently, this uses a peer-to-peer network, but with the 
 user who opened this waiting room being designated as the "host".
 The host basically acts as a regular user in the network, but will 
 be in charge of updating the AbstractWorld. While this does give them the 
 advantage of not having to deal with latency, I sincerely doubt 
 Orpheus will become an e-sport any time soon.
 * 
 * @author Matt Crow
 */
public class WaitingRoomBackend {
    public static final int WAIT_TIME = 0; //in seconds
    
    private final WSWaitForPlayers host;
    private final OrpheusServer server;
    
    /*
    these prototype teams allow this to keep track of
    which user wants to be on each team.
    The key is the user's IP address, while the value
    is a local copy of that user's remote User object.
    */
    private final HashMap<String, User> teamProto;
    
    /*
    this is the real team, constucted once the host
    sends the request for Build data
    */
    private final Team playerTeam;
    
    private Team enemyTeam; //move this to AbstractWorld or Battle later
    
    private int numWaves;
    private int maxEnemyLevel;
    private boolean isHost;
    private boolean gameAboutToStart;
    
    //all of these link to the method with the same name
    private final Consumer<ServerMessage> receiveInit;
    private final Consumer<ServerMessage> receiveUpdate;
    private final Consumer<ServerMessage> receiveBuildRequest;
    private final Consumer<ServerMessage> receiveRemoteIds;
    private final Consumer<ServerMessage> receiveWorldInit;
    
    
    
    public WaitingRoomBackend(WSWaitForPlayers page){
        host = page;
        server = Master.SERVER;
        isHost = false;
        numWaves = 1;
        maxEnemyLevel = 10;
        gameAboutToStart = false;
        
        teamProto = new HashMap<>();
        playerTeam = new Team("Players", Color.green);
        enemyTeam = null;
        
        receiveInit = (sm)->{
            receiveInit(sm);
        };
        receiveUpdate = (sm)->{
            receiveUpdate(sm);
        };
        receiveBuildRequest = (sm)->{
            receiveBuildRequest(sm);
        };
        receiveRemoteIds = (sm)->{
            receiveRemoteId(sm);
        };
        receiveWorldInit = (sm)->{
            receiveWorldInit(sm);
        };
    }
    
    /**
     * Initializes the server as a client,
     * that is to say, not the host.
     * Then, sets up the appropriate receivers.
     * 
     * @throws java.io.IOException
     */
    public void initClientServer() throws IOException{
        if(!server.isStarted()){
            server.start();
        } else {
            server.reset();
        }
        
        clearData();
        
        server.addReceiver(ServerMessageType.WAITING_ROOM_INIT, receiveInit);
        server.addReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
        server.addReceiver(ServerMessageType.REQUEST_PLAYER_DATA, receiveBuildRequest);
        isHost = false;
    }
    
    private void clearData(){
        teamProto.clear();
        playerTeam.clear();
        gameAboutToStart = false;
    }
    
    /**
     * Not applied to the host. Allows clients to
     * receive the waiting room initilization message
     * that the host sends upon a user connecting.
     * This message updates the client on the current
     * state of the waiting room at the time they joined.
     * @param sm 
     */
    private void receiveInit(ServerMessage sm){
        JsonObject obj = JsonUtil.fromString(sm.getBody());
        JsonUtil.verify(obj, "num waves");
        JsonUtil.verify(obj, "max enemy level");
        JsonUtil.verify(obj, "team");

        setNumWaves(obj.getInt("num waves"));
        setMaxEnemyLevel(obj.getInt("max enemy level"));
        obj.getJsonArray("team").stream().forEach((jv)->{
            if(jv.getValueType().equals(JsonValue.ValueType.OBJECT)){
                joinPlayerTeam(User.deserializeJson((JsonObject)jv));
            }
        });
        
        server.removeReceiver(ServerMessageType.WAITING_ROOM_INIT, receiveInit);
    }
    
    /**
     * Notifies that a user has joined the team.
     * The messages this responds to are sent by
     * @see WaitingRoomBackend#joinPlayerTeam(controllers.User) 
     * @param sm 
     */
    private void receiveUpdate(ServerMessage sm){
        //update message is now just 'join team'
        if(sm.getBody().equals("join player team")){
            joinPlayerTeam(sm.getSender());
        } else {
            System.out.println("not sure how to handle this: ");
            sm.displayData();
        }
    }
    
    /**
     * called after the host requests this user's Build data.
     * not applied to the host. Notifies the user that the host 
 needs their Build information.
 The game is about to start, so shut down most receivers
 also, prepare to receive IDs and AbstractWorld info from the host.
     * 
     * @param sm 
     */
    private synchronized void receiveBuildRequest(ServerMessage sm){
        server.send(
            new ServerMessage(
                BuildJsonUtil.serializeJson(host.getSelectedBuild()).toString(),
                ServerMessageType.PLAYER_DATA
            ),
            sm.getIpAddr()
        );
        host.setInputEnabled(false);
        
        server.removeReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
        server.removeReceiver(ServerMessageType.REQUEST_PLAYER_DATA, receiveBuildRequest);
        
        server.addReceiver(ServerMessageType.NOTIFY_IDS, receiveRemoteIds);
        server.addReceiver(ServerMessageType.WORLD_INIT, receiveWorldInit);
    }
    
    /**
     * Doesn't apply to the host.
     * Since the Player this user is going to be controlling
     * is on another computer, this needs some way of knowing
     * the IDs of the team and player this user is controlling
     * on that computer.
    */
    private void receiveRemoteId(ServerMessage sm){
        Master.getUser().setRemotePlayerId(sm.getBody());
        
        server.removeReceiver(ServerMessageType.NOTIFY_IDS, receiveRemoteIds);
    }
    
    /**
     * allows remote users to receive and de-serialize 
 the AbstractWorld created by the host.
     * 
     * this method is currently having problems, as the enemy team might not serialize,
     * and it takes a couple seconds to load teams into the world
     * 
     * @param sm 
     */
    private void receiveWorldInit(ServerMessage sm){
        WorldContent w = WorldContent.fromSerializedString(sm.getBody());
        RemoteProxyWorld world = new RemoteProxyWorld(w);
        try {
            world.setRemoteHost(sm.getIpAddr());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        User me = Master.getUser(); //need to set player before calling createCanvas
        me.linkToRemotePlayerInWorld(world);
        world.createCanvas();
        world.setCurrentMinigame(new Battle(maxEnemyLevel, numWaves)); // do I really need this?
        w.init();
        
        server.removeReceiver(ServerMessageType.WORLD_INIT, receiveWorldInit);
        
        WorldPage p = new WorldPage();
        WorldCanvas canv = world.getCanvas();
        canv.addPlayerControls(new RemotePlayerControls(me.getPlayer(), sm.getIpAddr()));
        canv.setPauseEnabled(false);
        p.setCanvas(canv);
        host.getHost().switchToPage(p);
    }
    
    public boolean isAlreadyStarted(){
        return gameAboutToStart;
    }
    
    public boolean isHost(){
        return isHost;
    }
    
    public void setNumWaves(int n){
        if(n >= 1){
            numWaves = n;
        } else {
            throw new IllegalArgumentException("Must have at least one wave");
        }
    }
    
    public void setMaxEnemyLevel(int l){
        if(l >= 1){
            maxEnemyLevel = l;
        } else {
            throw new IllegalArgumentException("Enemies must be at least level 1");
        }
    }
    
    public User[] getTeamProto(){
        return teamProto.values().stream().toArray(size -> new User[size]);
    }
    
    /**
     * Puts the given user on the prototype player team,
     * if they are not already there. Will change this to
     * automatically run once the player connects to the server.
     * 
     * @param u
     * @return 
     */
    public final WaitingRoomBackend joinPlayerTeam(User u){
        if(!teamProto.containsKey(u.getIpAddress())){
            //hasn't joined yet
            teamProto.put(u.getIpAddress(), u);
            
            if(u.equals(Master.getUser())){
                //only send an update if the user is the one who changed teams. Prevents infinite loop
                ServerMessage sm = new ServerMessage(
                    "join player team",
                    ServerMessageType.WAITING_ROOM_UPDATE
                );
                Master.SERVER.send(sm);
            }
            //host.updateTeamDisplays();
        }
        return this;
    }
    
    /**
     * Sends information about the waiting room to standard output
     */
    public final void displayData(){
        System.out.println("WAITING ROOM");
        System.out.println("Num waves: " + numWaves);
        System.out.println("Max enemy level: " + maxEnemyLevel);
        System.out.println("Players: ");
        teamProto.values().forEach((member)->System.out.println("--" + member.getName()));
        System.out.println("END OF WAITING ROOM");
    }
}
