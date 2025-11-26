package net.protocols;

import java.util.HashSet;
import java.util.Optional;

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
import world.WorldBuilderImpl;
import world.battle.Team;
import world.game.Game;

/**
 * The WaitingRoomHostProtocol is used to prepare a multiplayer game.
 * The process of this protocol is as follows:
 * 1. Whenever someone connects to the server, adds them to the player team.
 * 2. Once someone clicks the start button, the server asks for everyone's build.
 * 3. After each Build has been received, launches the world and tells everyone about it. 
 * @author Matt Crow
 */
public class WaitingRoomHostProtocol extends MessageHandler {    
    
    private final SpecificationResolver specificationResolver;
    private final Team playerTeam = Team.ofPlayers();
    private final HashSet<User> awaitingBuilds = new HashSet<>();
    private final WaitingRoom waitingRoom = new WaitingRoom();
    private final World world;
    
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
        this.specificationResolver = specificationResolver;
        
        world = new WorldBuilderImpl()
            .withGame(game)
            .withPlayers(playerTeam)
            .withAi(Team.ofAi())
            .build();

        addHandler(ServerMessageType.PLAYER_JOINED, this::receiveJoin);
        addHandler(ServerMessageType.START_WORLD, this::prepareToStart);
        addHandler(ServerMessageType.PLAYER_DATA, this::receiveBuildInfo);
    }

    @Override
    public OrpheusServer getServer(){
        AbstractNetworkClient anc = super.getServer();
        if(!(anc instanceof OrpheusServer)){
            throw new UnsupportedOperationException("WaitingRoomHostProtocol must run on an OrpheusServer");
        }
        return (OrpheusServer)anc;
    }
    
    private synchronized void receiveJoin(ServerMessagePacket sm){
        
        if(waitingRoom.containsUser(sm.getSender())){
            // already joined, so ignore the message
            return;
        }

        // tell everyone about the new guy
        User joiningUser = sm.getSender();
        waitingRoom.addUser(joiningUser);
        awaitingBuilds.add(joiningUser);
        Message sm1 = new Message(
            joiningUser.toJson().toString(),
            ServerMessageType.WAITING_ROOM_UPDATE
        );
        getServer().send(sm1);
        
        // tell the new guy about everyone else
        JsonObjectBuilder initMsgBuild = Json.createObjectBuilder();
        initMsgBuild.add("type", "waiting room init");
        JsonArrayBuilder userListBuild = Json.createArrayBuilder();
        for(User u : waitingRoom.getAllUsers()){
            userListBuild.add(u.toJson());
        }
        initMsgBuild.add("team", userListBuild.build());
        
        Message initMsg = new Message(
            initMsgBuild.build().toString(),
            ServerMessageType.WAITING_ROOM_INIT
        );
        getServer().send(initMsg, sm.getSender());
    }
    
    public final void prepareToStart(){
        getServer().send(new Message(ServerMessageType.REQUEST_PLAYER_DATA));
    }
    
    private synchronized void receiveBuildInfo(ServerMessagePacket sm){
        // synchronized avoids duplicate player IDs
        
        User sender = sm.getSender();

        if (!awaitingBuilds.contains(sender)) {
            // we don't need their build, so ignore it
            return;
        }
        
        var deserializer = new SpecificationJsonDeserializer();
        var json = JsonUtil.fromString(sm.getMessage().getBodyText());
        var specification = deserializer.fromJson(json);
        var assembledBuild = specificationResolver.resolve(specification);
        var player = Player.makeHuman(
            world,
            assembledBuild,
            sender.getId()
        );
        awaitingBuilds.remove(sender);
        playerTeam.addMember(player);

        // check if we have everyone's build, and thus are ready to start
        
        if (!awaitingBuilds.isEmpty()) {
            // we're still waiting on someone's build, so don't start yet
            return;
        }

        var server = getServer();
        
        world.init();
        
        var protocol = new HostWorldProtocol(server, world);
        server.setMessageHandler(Optional.of(protocol));
        server.send(new Message(ServerMessageType.WORLD, world.toGraph().toJson()));
    }
}
