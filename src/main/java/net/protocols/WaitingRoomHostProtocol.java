package net.protocols;

import world.battle.Battle;
import world.battle.Team;
import world.build.Build;
import world.build.BuildJsonUtil;
import world.entities.HumanPlayer;
import java.awt.Color;
import java.io.IOException;
import static java.lang.System.err;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import net.OrpheusServer;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import serialization.JsonUtil;
import users.AbstractUser;
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
public class WaitingRoomHostProtocol extends AbstractWaitingRoomProtocol<OrpheusServer>{    
    /*
    measured in seconds
     */
    public static final int WAIT_TIME = 3;
    
    private final Battle minigame;
    private final Team playerTeam;
    private HostWorld world; // may be null at some points
    
    /*
    The Users who have joined the waiting room, but have
    not yet sent their Builds to the server
    */
    private final HashSet<AbstractUser> awaitingBuilds;
    
    /**
     * Creates the protocol.
     * @param runningServer
     * @param game the game which players will play once this protocol is done.
     */
    public WaitingRoomHostProtocol(OrpheusServer runningServer, Battle game){
        super(runningServer);
        minigame = game;   
        playerTeam = new Team("Players", Color.blue);
        awaitingBuilds = new HashSet<>();
    }
    
    @Override
    public boolean receiveMessage(ServerMessagePacket sm, OrpheusServer forServer) {
        boolean handled = true;
        
        switch(sm.getMessage().getType()){
            case PLAYER_JOINED:
                receiveJoin(sm);
                break;
            case START_WORLD:
                prepareToStart();
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
    
    /**
     * Puts the given user on the teamProto,
     * and alerts all connected players
     * @param u the User who wants to play
     */
    public final void addUserToTeam(AbstractUser u){
        if(addToTeamProto(u)){
            awaitingBuilds.add(u);
            ServerMessage sm = new ServerMessage(
                u.serializeJson().toString(),
                ServerMessageType.WAITING_ROOM_UPDATE
            );
            getServer().send(sm);
        }
    }
    
    
    public final void prepareToStart(){
        playerTeam.clear();
        world = new HostWorld(
            getServer(), 
            WorldContent.createDefaultBattle()
        );
        requestBuilds();
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
            player = new HumanPlayer(
                world.getContent(), // world should not be null by now
                sender.getName()
            );
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
    
    
    /**
     * Once all Builds have been obtained, finally starts
     */    
    private void checkIfReady(){
        if(awaitingBuilds.isEmpty()){
            try {
                launchWorld();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private void launchWorld() throws IOException{
        //world.createCanvas();
        Team enemyTeam = new Team("AI", Color.red);
        world.getContent().setPlayerTeam(playerTeam);
        world.getContent().setAITeam(enemyTeam);
        world.getContent().setMinigame(minigame);
        minigame.setHost(world.getContent());
        world.init();
        
        HostWorldProtocol protocol = new HostWorldProtocol(getServer(), world);
        getServer().setProtocol(protocol);
        sendWorldInit(world.getContent());
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
}
