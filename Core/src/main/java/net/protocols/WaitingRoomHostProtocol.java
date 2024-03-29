package net.protocols;

import java.io.IOException;
import java.util.HashSet;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import net.AbstractNetworkClient;
import net.OrpheusServer;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import orpheus.core.champions.SpecificationJsonDeserializer;
import orpheus.core.champions.SpecificationResolver;
import orpheus.core.net.messages.Message;
import orpheus.core.users.User;
import orpheus.core.world.occupants.players.Player;
import serialization.JsonUtil;
import world.World;
import world.WorldBuilder;
import world.WorldBuilderImpl;
import world.battle.Team;
import world.game.Game;

/**
 * The WaitingRoomHostProtocol is used to prepare a multiplayer game.
 * The process of this protocol is as follows:
 * 1. Whenever someone connects to the server, adds them to the player team.
 * 2. Once the host clicks the start button, users are notified that the game will start soon.
 * 3. Once the time has elapsed, sends requests for the Builds of each connected LocalUser.
 * 4. After each Build has been received, creates a new World, applies each LocalUser's Build to a HumanPlayer in that World, 
 * and sends them information needed to remotely control that HumanPlayer. Serializes and sends the world as well.
 * @author Matt Crow
 */
public class WaitingRoomHostProtocol extends AbstractWaitingRoomProtocol {    
    
    private final Game minigame;

    /**
     * resolves the specifications received from players as JSON
     */
    private final SpecificationResolver specificationResolver;

    private final Team playerTeam;
    private World world; // may be null at some points
    
    /*
    The Users who have joined the waiting room, but have
    not yet sent their Builds to the server
    */
    private final HashSet<User> awaitingBuilds;
    
    /**
     * Creates the protocol.
     * @param runningServer
     * @param game the game which players will play once this protocol is done.
     */
    public WaitingRoomHostProtocol(
        OrpheusServer runningServer, 
        Game game,
        SpecificationResolver specificationResolver
    ){
        super(runningServer);
        minigame = game;
        this.specificationResolver = specificationResolver;
        playerTeam = Team.ofPlayers();
        awaitingBuilds = new HashSet<>();
    }

    @Override
    public OrpheusServer getServer(){
        AbstractNetworkClient anc = super.getServer();
        if(!(anc instanceof OrpheusServer)){
            throw new UnsupportedOperationException("WaitingRoomHostProtocol must run on an OrpheusServer");
        }
        return (OrpheusServer)anc;
    }
    
    @Override
    public boolean receive(ServerMessagePacket sm) {
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
        User joiningUser = sm.getSender();
        addUserToTeam(joiningUser);
        
        /*
        Second, create the init message to bring 
        the joining user up to date on the current 
        waiting room status
        */
        JsonObjectBuilder initMsgBuild = Json.createObjectBuilder();
        initMsgBuild.add("type", "waiting room init");
        JsonArrayBuilder userListBuild = Json.createArrayBuilder();
        for(User u : getTeamProto()){
            userListBuild.add(u.toJson());
        }
        initMsgBuild.add("team", userListBuild.build());
        
        Message initMsg = new Message(
            initMsgBuild.build().toString(),
            ServerMessageType.WAITING_ROOM_INIT
        );
        getServer().send(initMsg, sm.getSender());
    }
    
    private void addUserToTeam(User u){
        if(addToTeamProto(u)){
            awaitingBuilds.add(u);
            Message sm = new Message(
                u.toJson().toString(),
                ServerMessageType.WAITING_ROOM_UPDATE
            );
            getServer().send(sm);
        }
    }
    
    public final void prepareToStart(){
        playerTeam.clear();
        WorldBuilder worldBuilder = new WorldBuilderImpl();
        
        world = worldBuilder
            .withGame(minigame)
            .withPlayers(playerTeam)
            .withAi(Team.ofAi())
            .build();
    
        getServer().send(new Message(
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
    private synchronized void receiveBuildInfo(ServerMessagePacket sm){
        // synchronized avoids duplicate player IDs
        
        User sender = sm.getSender();

        if (!awaitingBuilds.contains(sender)) {
            throw new RuntimeException(String.format("received second build from %s, expected only once", sender.getName()));
        }
        
        var deserializer = new SpecificationJsonDeserializer();
        var json = JsonUtil.fromString(sm.getMessage().getBodyText());
        var specification = deserializer.fromJson(json);
        var assembledBuild = specificationResolver.resolve(specification);
        var player = Player.makeHuman(
            world, // world should not be null by now,
            assembledBuild,
            sender.getId()
        );
        awaitingBuilds.remove(sender);
        playerTeam.addMember(player);

        checkIfReady();
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
        HostWorldUpdater updater = new HostWorldUpdater(getServer(), world);
        
        world.init();
        
        HostWorldProtocol protocol = new HostWorldProtocol(getServer(), world);
        getServer().setProtocol(protocol);
        getServer().send(new Message(ServerMessageType.WORLD, world.toGraph().toJson()));
        
        updater.start();
    }
}
