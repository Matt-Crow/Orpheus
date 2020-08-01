package net.protocols;

import battle.Battle;
import battle.Team;
import controls.userControls.SoloPlayerControls;
import customizables.Build;
import customizables.BuildJsonUtil;
import entities.HumanPlayer;
import java.awt.Color;
import java.io.IOException;
import static java.lang.System.err;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.swing.Timer;
import net.OrpheusServer;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import serialization.JsonUtil;
import users.AbstractUser;
import users.LocalUser;
import gui.pages.worldSelect.HostWaitingRoom;
import gui.pages.worldPlay.WorldCanvas;
import gui.pages.worldPlay.WorldPage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import net.messages.ServerMessage;
import world.HostWorld;
import world.WorldContent;

/**
 * The WaitingRoomHostProtocol is used to prepare a multiplayer game.
 * The process of this protocol is as follows:
 * 1. Whenever someone connects to the server, adds them to the player team.
 * 2. Once the host clicks the start button, users are notified that the game will start soon.
 * 3. Once the time has elapsed, sends requests for the Builds of each connected User.
 * 4. After each Build has been received, creates a new World, applies each User's Build to a HumanPlayer in that World, 
 * and sends them information needed to remotely control that HumanPlayer. Serializes and sends the world as well.
 * @author Matt Crow
 */
public class WaitingRoomHostProtocol extends AbstractWaitingRoomProtocol{    
    /*
    measured in seconds
     */
    public static final int WAIT_TIME = 3;
    
    private final Battle minigame;
    private final Team playerTeam;
    private final Team enemyTeam;
    private HumanPlayer myPlayer;
    
    /*
    The Users who have joined the waiting room, but have
    not yet sent their Builds to the server
    */
    private final HashMap<InetAddress, AbstractUser> awaitingBuilds;
    
    /**
     * Creates the protocol.
     * @param host the HostWaitingRoom to act as this protocol's front end
     * @param game the game which players will play once this protocol is done.
     */
    public WaitingRoomHostProtocol(HostWaitingRoom host, Battle game){
        super(host);
        minigame = game;   
        playerTeam = new Team("Players", Color.blue);
        enemyTeam = new Team("AI", Color.red);
        awaitingBuilds = new HashMap<>();
    }
    
    /**
     * Prepares the waiting room to receive players.
     */
    @Override
    public void doApplyProtocol(){
        playerTeam.clear();
        enemyTeam.clear();
        awaitingBuilds.clear();
        myPlayer = null;
        
        try {
            getFrontEnd().getChat().openChatServer();
        } catch (IOException ex) {
            getFrontEnd().getChat().logLocal("Failed to open chat server");
            ex.printStackTrace();
        }
    }
    
    /**
     * Puts the given user on the teamProto,
     * and alerts all connected players
     * @param u the User who wants to play
     */
    public final void addUserToTeam(AbstractUser u){
        if(addToTeamProto(u)){
            awaitingBuilds.put(u.getIpAddress(), u);
            ServerMessage sm = new ServerMessage(
                "join player team",
                ServerMessageType.WAITING_ROOM_UPDATE
            );
            OrpheusServer.getInstance().send(sm);
            getFrontEnd().getChat().logLocal(u.getName() + " has joined the team!");
            getFrontEnd().updateTeamDisplays();
        }
    }
    
    /**
     * Called whenever a new player connects to the server.
     * Adds them to the player team, 
     * notifies the other players,
     * and sends the new player the current waiting room status.
     * 
     * @param sm
     */
    private void receiveJoin(ServerMessagePacket sm){
        InetAddress joiningIp = sm.getSendingIp();
        if(containsIp(joiningIp)){
            return;
        } // ##################### RETURNS HERE IF ALREADY JOINED
        
        /*
        First, add the new player to the player team.
        addUserToTeam automatically notifies everone 
        connected that someone has joined
        */
        AbstractUser joiningUser = sm.getSender();
        addUserToTeam(joiningUser);
        
        /*
        Second, create the init message to bring 
        the joining user up to date on the current 
        waiting room status
        */
        JsonObjectBuilder initMsgBuild = Json.createObjectBuilder();
        initMsgBuild.add("type", "waiting room init");
        JsonArrayBuilder userListBuild = Json.createArrayBuilder();
        for(AbstractUser u : getTeamProto()){
            userListBuild.add(u.serializeJson());
        }
        initMsgBuild.add("team", userListBuild.build());
        
        ServerMessage initMsg = new ServerMessage(
            initMsgBuild.build().toString(),
            ServerMessageType.WAITING_ROOM_INIT
        );
        OrpheusServer.getInstance().send(initMsg, joiningIp);
    }
    
    public final void prepareToStart(){
        ((HostWaitingRoom)getFrontEnd()).setStartButtonEnabled(false);
        playerTeam.clear();
        Timer t = new Timer(WaitingRoomHostProtocol.WAIT_TIME * 1000, (e)->{
            waitForData();
            requestBuilds();
        });
        t.setRepeats(false);
        t.start();
    }
    
    /**
     * Begins constructing teams, and sends a request for user Build data.
     * Once all Builds have been obtained, finally starts
     */
    private void waitForData(){
        getFrontEnd().setInputEnabled(false);
        
        // put the host on the user team
        LocalUser me = LocalUser.getInstance();
        if(awaitingBuilds.containsKey(me.getIpAddress())){
            myPlayer = new HumanPlayer(me.getName());
            myPlayer.applyBuild(getFrontEnd().getSelectedBuild());
            playerTeam.addMember(myPlayer);
            awaitingBuilds.remove(me.getIpAddress());
        } else {
            throw new UnsupportedOperationException();
        }
        
        // check if they were the only player
        checkIfReady();
    }
    
    private void requestBuilds(){
        OrpheusServer.getInstance().send(new ServerMessage(
            "please provide build information",
            ServerMessageType.REQUEST_PLAYER_DATA
        ));
    }
    
    /**
     * called after the host receives a user's Build information.
     * 
     * applies the Build to a HumanPlayer which will be controlled
     * by the message's sender. After constructing the new player,
     * adds them to the team, then notifies the message's
     * sender of what their player ID is on this remote computer
     * 
     * @param sm a server message containing the sender's Build, serialized as a JSON object string
     */
    private void receiveBuildInfo(ServerMessagePacket sm){
        InetAddress ip = sm.getSendingIp();
        HumanPlayer player = null;
        Build b;
        
        if(awaitingBuilds.containsKey(ip)){
            player = new HumanPlayer(sm.getSender().getName());
            awaitingBuilds.remove(ip);
        } else {
            err.println("Ugh oh, " + sm.getSender().getName() + " isn't on any team!");
            return;
        }

        b = BuildJsonUtil.deserializeJson(JsonUtil.fromString(sm.getBody()));
        player.applyBuild(b);
        playerTeam.addMember(player);
        
        sendRemoteId(ip, player.id);
        checkIfReady();
    }
    
    /**
     * Called by receiveBuild
     * @param ipAddr the ip address of the user to send the IDs to.
     * @param playerId the ID of that user's Player on this computer
     */
    private void sendRemoteId(InetAddress ipAddr, String playerId){
        ServerMessage sm = new ServerMessage(
            playerId,
            ServerMessageType.NOTIFY_IDS
        );
        OrpheusServer.getInstance().send(sm, ipAddr);
    }
    
    private void checkIfReady(){
        if(awaitingBuilds.isEmpty()){
            try {
                createWorld();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Creates, sends, and switches to a new world
     * @throws IOException 
     */
    private void createWorld() throws IOException{
        HostWorld w = new HostWorld(WorldContent.createDefaultBattle());
        w.createCanvas();
        w.setPlayerTeam(playerTeam).setEnemyTeam(enemyTeam).setCurrentMinigame(minigame);
        minigame.setHost(w.getContent());
        w.init();
        
        new HostWorldProtocol(w).applyProtocol();
        sendWorldInit(w.getContent());
        
        WorldPage p = new WorldPage();
        WorldCanvas canv = w.getCanvas();
        
        canv.addPlayerControls(new SoloPlayerControls(w, myPlayer.id));
        canv.setPauseEnabled(false);
        p.setCanvas(canv);
        getFrontEnd().getHost().switchToPage(p);
    }
    
    /**
     * Serializes the worldContent, and sends it
     * to each connected user, excluding the host
     * @param w the world to send
     */
    private void sendWorldInit(WorldContent w){
        String serial = w.serializeToString();
        ServerMessage sm = new ServerMessage(
            serial,
            ServerMessageType.WORLD_INIT
        );
        OrpheusServer.getInstance().send(sm);
    }
    
    @Override
    public boolean receiveMessage(ServerMessagePacket sm, OrpheusServer forServer) {
        boolean handled = true;
        
        switch(sm.getType()){
            case PLAYER_JOINED:
                receiveJoin(sm);
                break;
            case PLAYER_DATA:
                receiveBuildInfo(sm);
                break;
            default:
                handled = false;
                break;
        }
        
        return handled;
    }

}
