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
import windows.WorldSelect.WSWaitForPlayers;
import windows.world.WorldCanvas;
import windows.world.WorldPage;
import world.HostWorld;
import world.WorldContent;

/**
 *
 * @author Matt
 */
public class WaitingRoomHostBuildProtocol implements AbstractOrpheusServerProtocol{
    /*
    measured in seconds
    */
    private static final int WAIT_TIME = 0;
    private final WSWaitForPlayers frontEnd;
    private final OrpheusServer server;
    private final Team playerTeam;
    private final Team enemyTeam;
    private final Battle game;
    private final HashMap<String, User> awaitingBuilds;
    
    public WaitingRoomHostBuildProtocol(WSWaitForPlayers host, OrpheusServer forServer, HashMap<String, User> users, int waveCount, int maxEnemyLv){
        frontEnd = host;
        server = forServer;
        playerTeam = new Team("Players", Color.blue);
        enemyTeam = new Team("AI", Color.red);
        awaitingBuilds = new HashMap<>();
        users.forEach((ip, user)->awaitingBuilds.put(ip, user));
        game = new Battle(maxEnemyLv, waveCount);
    }
    
    public final void start(){
        frontEnd.setStartButtonEnabled(false);
        playerTeam.clear();
        Timer t = new Timer(WAIT_TIME * 1000, (e)->{
            waitForData();
            requestBuilds();
        });
        t.setRepeats(false);
        t.start();
    }
    
    private void waitForData(){
        frontEnd.setInputEnabled(false);
        
        // put the host on the user team
        Master.getUser().initPlayer().getPlayer().applyBuild(frontEnd.getSelectedBuild());
        if(awaitingBuilds.containsKey(server.getIpAddr())){
            playerTeam.addMember(Master.getUser().getPlayer());
            awaitingBuilds.remove(server.getIpAddr());
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
    
    private void checkIfReady(){
        if(awaitingBuilds.isEmpty()){
            try {
                createWorld();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
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
    
    private void sendRemoteId(String ipAddr, String playerId){
        ServerMessage sm = new ServerMessage(
            playerId,
            ServerMessageType.NOTIFY_IDS
        );
        server.send(sm, ipAddr);
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
        frontEnd.getHost().switchToPage(p);
    }
    
    private void sendWorldInit(WorldContent w){
        String serial = w.serializeToString();
        ServerMessage sm = new ServerMessage(
            serial,
            ServerMessageType.WORLD_INIT
        );
        server.send(sm);
    }
}
