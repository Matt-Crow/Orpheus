package net.protocols;

import world.battle.Battle;
import world.battle.Team;
import controls.userControls.SoloPlayerControls;
import world.customizables.Build;
import world.customizables.BuildJsonUtil;
import world.entities.HumanPlayer;
import java.awt.Color;
import java.io.IOException;
import static java.lang.System.err;
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
import java.util.HashSet;
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
    private final HashSet<AbstractUser> awaitingBuilds;
    
    /**
     * Creates the protocol.
     * @param runningServer
     * @param host the HostWaitingRoom to act as this protocol's front end
     * @param game the game which players will play once this protocol is done.
     */
    public WaitingRoomHostProtocol(OrpheusServer runningServer, HostWaitingRoom host, Battle game){
        super(runningServer, host);
        minigame = game;   
        playerTeam = new Team("Players", Color.blue);
        enemyTeam = new Team("AI", Color.red);
        awaitingBuilds = new HashSet<>();
    }
    
    /**
     * Puts the given user on the teamProto,
     * and alerts all connected players
     * @param u the User who wants to play
     */
    public final void addUserToTeam(AbstractUser u){
        if(addToTeamProto(u)){
            awaitingBuilds.add(u);
            ServerMessage sm = new ServerMessage(
                "join player team",
                ServerMessageType.WAITING_ROOM_UPDATE
            );
            getServer().send(sm);
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
        if(containsUser(sm.getSender())){
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
        getServer().send(initMsg, sm.getSender());
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
        if(awaitingBuilds.contains(me)){
            myPlayer = new HumanPlayer(me.getName());
            myPlayer.applyBuild(getFrontEnd().getSelectedBuild());
            playerTeam.addMember(myPlayer);
            awaitingBuilds.remove(me);
        } else {
            throw new UnsupportedOperationException();
        }
        
        // check if they were the only player
        checkIfReady();
    }
    
    private void requestBuilds(){
        getServer().send(new ServerMessage(
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
        HumanPlayer player = null;
        AbstractUser sender = sm.getSender();
        
        if(awaitingBuilds.contains(sender)){
            player = new HumanPlayer(sender.getName());
            awaitingBuilds.remove(sender);
        } else {
            err.println("Ugh oh, " + sender.getName() + " isn't on any team!");
            return;
        }

        Build b = BuildJsonUtil.deserializeJson(JsonUtil.fromString(sm.getMessage().getBody()));
        player.applyBuild(b);
        playerTeam.addMember(player);
        
        sendRemoteId(sender, player.id);
        checkIfReady();
    }
    
    /**
     * Called by receiveBuild
     * @param ipAddr the user to send the IDs to.
     * @param playerId the ID of that user's Player on this computer
     */
    private void sendRemoteId(AbstractUser user, String playerId){
        ServerMessage sm = new ServerMessage(
            playerId,
            ServerMessageType.NOTIFY_IDS
        );
        getServer().send(sm, user);
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
        HostWorld w = new HostWorld(getServer(), WorldContent.createDefaultBattle());
        w.createCanvas();
        w.setPlayerTeam(playerTeam).setEnemyTeam(enemyTeam).setCurrentMinigame(minigame);
        minigame.setHost(w.getContent());
        w.init();
        
        HostWorldProtocol protocol = new HostWorldProtocol(getServer(), w);
        getServer().setProtocol(protocol);
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
        getServer().send(sm);
    }
    
    @Override
    public boolean receiveMessage(ServerMessagePacket sm, OrpheusServer forServer) {
        boolean handled = true;
        
        switch(sm.getMessage().getType()){
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
