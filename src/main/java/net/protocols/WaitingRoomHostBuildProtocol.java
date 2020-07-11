package net.protocols;

import battle.Battle;
import battle.Team;
import controllers.Master;
import controllers.User;
import controls.SoloPlayerControls;
import customizables.Build;
import customizables.BuildJsonUtil;
import entities.HumanPlayer;
import java.awt.Color;
import java.io.IOException;
import static java.lang.System.err;
import java.util.HashMap;
import javax.swing.Timer;
import net.OrpheusServer;
import net.ServerMessage;
import net.ServerMessageType;
import serialization.JsonUtil;
import windows.WorldSelect.HostWaitingRoom;
import windows.world.WorldCanvas;
import windows.world.WorldPage;
import world.HostWorld;
import world.WorldContent;

/**
 *
 * @author Matt
 */
public class WaitingRoomHostBuildProtocol extends AbstractWaitingRoomProtocol{
    /*
    measured in seconds
    */
    public static final int WAIT_TIME = 0;
    private final Team playerTeam;
    private final Team enemyTeam;
    private final Battle game;
    private final HashMap<String, User> awaitingBuilds;
    
    public WaitingRoomHostBuildProtocol(HostWaitingRoom host, OrpheusServer forServer, User[] users, Battle minigame){
        super(host, forServer);
        playerTeam = new Team("Players", Color.blue);
        enemyTeam = new Team("AI", Color.red);
        awaitingBuilds = new HashMap<>();
        for(User user : users){
            awaitingBuilds.put(user.getIpAddress(), user);
        }
        game = minigame;
    }
    
    /**
     * After WAIT_TIME seconds,
     * prepares the server to receive 
     * player builds.
     */
    public final void start(){
        ((HostWaitingRoom)getFrontEnd()).setStartButtonEnabled(false);
        playerTeam.clear();
        Timer t = new Timer(WAIT_TIME * 1000, (e)->{
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
        Master.getUser().initPlayer().getPlayer().applyBuild(getFrontEnd().getSelectedBuild());
        if(awaitingBuilds.containsKey(getServer().getIpAddr())){
            playerTeam.addMember(Master.getUser().getPlayer());
            awaitingBuilds.remove(getServer().getIpAddr());
        } else {
            throw new UnsupportedOperationException();
        }
        
        // check if they were the only player
        checkIfReady();
    }
    
    private void requestBuilds(){
        Master.SERVER.send(new ServerMessage(
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
    private void receiveBuildInfo(ServerMessage sm){
        String ip = sm.getIpAddr();
        HumanPlayer player;
        Build b;
        
        if(awaitingBuilds.containsKey(ip)){
            player = awaitingBuilds.get(ip).initPlayer().getPlayer();
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
    private void sendRemoteId(String ipAddr, String playerId){
        ServerMessage sm = new ServerMessage(
            playerId,
            ServerMessageType.NOTIFY_IDS
        );
        getServer().send(sm, ipAddr);
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
    
    @Override
    public boolean receiveMessage(ServerMessage sm, OrpheusServer forServer) {
        boolean received = true;
        switch(sm.getType()){
            case PLAYER_DATA:
                receiveBuildInfo(sm);
                break;
            default:
                received = false;
                break;
        }
        return received;
    }
    
    /**
     * Creates, sends, and switches to a new world
     * @throws IOException 
     */
    private void createWorld() throws IOException{
        HostWorld w = new HostWorld(WorldContent.createDefaultBattle());
        w.createCanvas();
        w.initServer();
        w.setPlayerTeam(playerTeam).setEnemyTeam(enemyTeam).setCurrentMinigame(game);
        game.setHost(w);
        w.init();
        
        sendWorldInit(w.getContent());
        
        WorldPage p = new WorldPage();
        WorldCanvas canv = w.getCanvas();
        canv.addPlayerControls(new SoloPlayerControls(Master.getUser().getPlayer()));
        canv.setPauseEnabled(false);
        p.setCanvas(canv);
        getFrontEnd().getHost().switchToPage(p);
        
        getServer().setProtocol(null);
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
