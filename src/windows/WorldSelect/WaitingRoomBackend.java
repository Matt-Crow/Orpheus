package windows.WorldSelect;

import battle.Battle;
import battle.Team;
import controllers.*;
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
import javax.json.*;
import javax.swing.*;
import net.*;
import serialization.JsonUtil;
import windows.world.WorldPage;

/**
 * This class provides all the server
 * functionality needed by WSWaitForPlayers,
 * as that class got a bit out of hand with
 * all the functionality in one file.
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
    
    //all of these link to the method with the same name
    private final Consumer<ServerMessage> receiveJoin;
    private final Consumer<ServerMessage> receiveInit;
    private final Consumer<ServerMessage> receiveUpdate;
    private final Consumer<ServerMessage> receiveBuildRequest;
    private final Consumer<ServerMessage> receiveBuildInfo;
    private final Consumer<ServerMessage> receiveRemoteIds;
    private final Consumer<ServerMessage> receiveWorldInit;
    
    
    
    public WaitingRoomBackend(WSWaitForPlayers page){
        host = page;
        server = Master.SERVER;
        isHost = false;
        teamSize = 0;
        gameAboutToStart = false;
        
        team1Proto = new HashMap<>();
        team2Proto = new HashMap<>();
        
        receiveJoin  = (sm)->{
            receiveJoin(sm);
        };
        receiveInit = (sm)->{
            receiveInit(sm);
        };
        receiveUpdate = (sm)->{
            receiveUpdate(sm);
        };
        receiveBuildRequest = (sm)->{
            receiveBuildRequest(sm);
        };
        receiveBuildInfo = (sm)->{
            receiveBuildInfo(sm);
        };
        receiveRemoteIds = (sm)->{
            receiveRemoteIds(sm);
        };
        receiveWorldInit = (sm)->{
            receiveWorldInit(sm);
        };
    }
    
    /**
     * Initializes the server as a host,
     * setting up the appropriate receivers.
     * This should only be called by one 
     * user in each game
     * 
     * @throws java.io.IOException if the server cannot be started
     */
    public void initHostServer() throws IOException{
        if(!server.isStarted()){
            server.start();
        } else {
            server.reset();
        }
        
        clearData();
        
        server.addReceiver(ServerMessageType.PLAYER_JOINED, receiveJoin);
        server.addReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
        isHost = true;
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
        team1Proto.clear();
        team2Proto.clear();
        team1 = null;
        team2 = null;
        gameAboutToStart = false;
    }
    
    /**
     * applied only to the host. Whenever a user
     * joins the server, notifies them of the 
     * current state of the waiting room.
     * This will then be caught by the client's
     * receiveInit method.
     * 
     * @param sm 
     */
    private void receiveJoin(ServerMessage sm){
        String ipAddr = sm.getIpAddr();
        
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
        
        server.send(initMsg, ipAddr);
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
        
        server.removeReceiver(ServerMessageType.WAITING_ROOM_INIT, receiveInit);
    }
    
    /**
     * Notifies that a user has changed teams.
     * The messages this responds to are sent by 
     * the tryJoinTeam methods
     * @see WaitingRoomBackend#tryJoinTeam1(controllers.User) 
     * @see WaitingRoomBackend#tryJoinTeam2(controllers.User) 
     * @param sm 
     */
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
    
    /**
     * called after the host requests this user's Build data.
     * not applied to the host. Notifies the user that the host 
     * needs their Build information.
     * The game is about to start, so shut down most receivers
     * also, prepare to receive IDs and World info from the host.
     * 
     * @param sm 
     */
    private synchronized void receiveBuildRequest(ServerMessage sm){
        server.send(
            new ServerMessage(
                host.getSelectedBuild().serializeJson().toString(),
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
     * Applies only to the host.
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
        String ip = sm.getIpAddr();
        TruePlayer tp;
        Build b;
        int teamNum;

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
        
        //team1.displayData();
        //team2.displayData();
        /*
        if(team1.getRosterSize() == teamSize){
            out.println("team 1 is done");
        }
        if(team2.getRosterSize() == teamSize){
            out.println("team 2 is done");
        }*/
        if(team1.getRosterSize() == teamSize && team2.getRosterSize() == teamSize){
            server.removeReceiver(ServerMessageType.PLAYER_DATA, receiveBuildInfo);
            finallyStart();
        }
    }
    
    /**
     * Doesn't apply to the host.
     * Since the Player this user is going to be controlling
     * is on another computer, this needs some way of knowing
     * the IDs of the team and player this user is controlling
     * on that computer.
    */
    private void receiveRemoteIds(ServerMessage sm){
        String[] split = sm.getBody().split(",");
        int tId = Integer.parseInt(split[0].replace("team:", "").trim());
        int pId = Integer.parseInt(split[1].replace("player:", "").trim());
        //System.out.printf("(receiveRemoteIds) OK, so my team's ID is %d, and my player ID is %d, right?", tId, pId);
        
        Master.getUser().setRemoteTeamId(tId).setRemotePlayerId(pId);
        
        server.removeReceiver(ServerMessageType.NOTIFY_IDS, receiveRemoteIds);
    }
    
    /**
     * allows remote users to receive and de-serialize 
     * the World created by the host.
     * 
     * this method is currently having problems, as the enemy team might not serialize,
     * and it takes a couple seconds to load teams into the world
     * 
     * @param sm 
     */
    private void receiveWorldInit(ServerMessage sm){
        World w = World.fromSerializedString(sm.getBody());
        try {
            w.setRemoteHost(sm.getIpAddr());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        User me = Master.getUser(); //need to set player before calling createCanvas
        me.linkToRemotePlayerInWorld(w);
        w.createCanvas();
        w.setCurrentMinigame(new Battle());
        w.init();
        
        server.removeReceiver(ServerMessageType.WORLD_INIT, receiveWorldInit);
        
        WorldPage p = new WorldPage(host.getHostingPage().getHost());
        p.setCanvas(w.getCanvas());
        host.getHostingPage().getHost().switchToPage(p);
    }
    
    
    
    /**
     * Begins preparing the server to receive
     * player build information.
     * After WAIT_TIME seconds, waits for data before starting the world
     */
    public void prepareToStart(){
        gameAboutToStart = true;
        host.setStartButtonEnabled(false);
        
        server.setAcceptingConn(false);
        server.removeReceiver(ServerMessageType.PLAYER_JOINED, receiveJoin);
        Timer t = new Timer(WAIT_TIME * 1000, (e)->{
            server.removeReceiver(ServerMessageType.WAITING_ROOM_UPDATE, receiveUpdate);
            waitForData();
        });
        t.setRepeats(false);
        t.start();
    }
    
    /**
     * Begins constructing teams, and sends a request for user Build data.
     * Once all Builds have been obtained, finally starts
     * Note that receiveBuildInfo calls finallyStart, not this method.
     * 
     * @see WSWaitForPlayers#receiveBuildInfo
     */
    private void waitForData(){
        //need these here, else null pointer in responding to build request
        team1 = Team.constructRandomTeam("Team 1", Color.green, teamSize - team1Proto.size());
        team2 = Team.constructRandomTeam("team 2", Color.red, teamSize - team2Proto.size());
        
        server.addReceiver(ServerMessageType.PLAYER_DATA, receiveBuildInfo);
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
        
        //team1.displayData();
        //team2.displayData();
        /*
        if(team1.getRosterSize() == teamSize){
            out.println("team 1 is done");
        }
        if(team2.getRosterSize() == teamSize){
            out.println("team 2 is done");
        }*/
        if(team1.getRosterSize() == teamSize && team2.getRosterSize() == teamSize){
            Master.SERVER.removeReceiver(ServerMessageType.PLAYER_DATA, receiveBuildInfo);
            finallyStart();
        }
    }
    
    /**
     * this starts the world (it's about time too!)
     * sends the serialized version of the newly created
     * World to all connected users, then switches to 
     * that new World's canvas
     * 
     * Might not be completely done
     */
    private void finallyStart(){
        World w = World.createDefaultBattle();
        try {
            w.setIsHosting(true);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        w.createCanvas(); //need to recreate so that togglepause doesn't work
        Battle b = new Battle();
        w.addTeam(team1).addTeam(team2).setCurrentMinigame(b);
        b.setHost(w);
        w.init();
        
        sendWorldInit(w);
        
        WorldPage p = new WorldPage(host.getHostingPage().getHost());
        p.setCanvas(w.getCanvas());
        host.getHostingPage().getHost().switchToPage(p);
    }
    
    /**
     * Called by waitForData
     * @see WaitingRoomBackend#waitForData() 
     */
    private void requestBuilds(){
        Master.SERVER.send(new ServerMessage(
            "please provide build information",
            ServerMessageType.REQUEST_PLAYER_DATA
        ));
    }
    
    /**
     * Called by receiveBuild
     * @param ipAddr the ip address of the user to send the IDs to.
     * @param teamId the ID of that user's Team on this computer
     * @param playerId the ID of that user's Player on this computer
     */
    private void sendIds(String ipAddr, int teamId, int playerId){
        ServerMessage sm = new ServerMessage(
            String.format("team: %d, player: %d", teamId, playerId),
            ServerMessageType.NOTIFY_IDS
        );
        Master.SERVER.send(sm, ipAddr);
    }
    
    /**
     * Serializes the world, and sends it
     * to each connected user, excluding the host
     * @param w the world to send
     */
    private void sendWorldInit(World w){
        String serial = w.serializeToString();
        ServerMessage sm = new ServerMessage(
            serial,
            ServerMessageType.WORLD_INIT
        );
        
        Master.SERVER.send(sm);
    }
    
    public boolean isAlreadyStarted(){
        return gameAboutToStart;
    }
    
    public boolean isHost(){
        return isHost;
    }
    
    public boolean team1Full(){
        return team1Proto.size() >= teamSize;
    }
    public boolean team2Full(){
        return team2Proto.size() >= teamSize;
    }
    
    public void setTeamSize(int s){
        if(s >= 1){
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
     * Places a User on team 1.
     * If said user is the person at this
     * computer, sends a message to all connected
     * clients that a team change has occurred.
     * 
     * @param u the User to place on team 1
     * @return whether or not the User was able to join team 1
     */
    public boolean tryJoinTeam1(User u){
        boolean success = team1Proto.containsKey(u.getIpAddress());
        //automatically return true if the player is already on the correct team
        
        if(!success && !team1Full()){
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
                Master.SERVER.send(sm);
            }
            host.updateTeamDisplays();
        }
        return success;
    }
    /**
     * Places a User on team 2.
     * If said user is the person at this
     * computer, sends a message to all connected
     * clients that a team change has occurred.
     * 
     * @param u the User to place on team 2
     * @return whether or not the User was able to join team 2
     */
    public boolean tryJoinTeam2(User u){
        boolean success = team2Proto.containsKey(u.getIpAddress());
        if(!team2Full() && !success){
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
                Master.SERVER.send(sm);
            }
            host.updateTeamDisplays();
        }
        return success;
    }
    
    /**
     * Sends information about the waiting room to standard output
     */
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
