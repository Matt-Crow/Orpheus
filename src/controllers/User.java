package controllers;

import entities.TruePlayer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonSerialable;
import serialization.JsonUtil;

/**
 * 
 * A User is meant to replace the TruePlayer class.
 * The class represents a real person- not just a Player entity
 * controlled by AI.
 * 
 * @author Matt Crow
 */
public final class User implements JsonSerialable{
    private String userName;
    private String ipAddr;
    private TruePlayer player;
    
    private int remoteTeamId; //the ID of the Team this' player is on on a remote computer
    private int remotePlayerId;
    
    private boolean isAdmin;
    
    //keep in mind that a user will have different IDs on each server
    private final int userId;
    private static int nextId = 0;
    public static final String DEFAULT_NAME = "name not set";
    
    public User(String name){
        userName = name;
        ipAddr = null;
        player = null;
        isAdmin = false;
        
        
        userId = nextId;
        nextId++;
    }
    
    public User(){
        this(DEFAULT_NAME);
    }
    
    public User setName(String s){
        userName = s;
        return this;
    }
    public String getName(){
        return userName;
    }
    
    public User setRemoteTeamId(int id){
        remoteTeamId = id;
        return this;
    }
    public int getRemoteTeamId(){
        return remoteTeamId;
    }
    
    public User setRemotePlayerId(int id){
        remotePlayerId = id;
        return this;
    }
    public int getRemotePlayerId(){
        return remotePlayerId;
    }
    
    /**
     * Used to re-associate this User with a Player
     * received from a serialized world
     * @param w
     * @return 
     */
    public User linkToRemotePlayerInWorld(World w){
        setPlayer((TruePlayer)w.getTeamById(remoteTeamId).getMemberById(remotePlayerId));
        return this;
    }
    
    /**
     * Creates a new Player entity, and associates it with this user
     * note that this does not call TruePlayer's init method
     * @return this, for chaining purposes
     */
    public User initPlayer(){
        player = new TruePlayer(userName);
        return this;
    }
    //used for receiving remote player
    public User setPlayer(TruePlayer p){
        player = p;
        return this;
    }
    public TruePlayer getPlayer(){
        if(player == null){
            initPlayer();
        }
        return player;
    }
    
    public String getIpAddress(){
        if(ipAddr == null){
            //temporary until startServer is done
            try {
                ipAddr = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ipAddr;
    }
    
    
    @Override
    public boolean equals(Object o){
        return o != null && o instanceof User && ((User)o).userId == userId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.userId;
        return hash;
    }

    @Override
    public JsonObject serializeJson() {
        JsonObjectBuilder obj = Json.createObjectBuilder();
        obj.add("type", "user");
        obj.add("name", userName);
        obj.add("ip address", getIpAddress());
        //obj.add("player", player.serializeJson());
        return obj.build();
    }
    
    public static User deserializeJson(JsonObject obj){
        JsonUtil.verify(obj, "type");
        JsonUtil.verify(obj, "name");
        JsonUtil.verify(obj, "ip address");
        //JsonUtil.verify(obj, "player");
        
        if(!obj.getString("type").equals("user")){
            throw new JsonException("not a user");
        }
        User ret = new User(obj.getString("name"));
        ret.ipAddr = obj.getString("ip address");
        //ret.player = Player.deserializeJson(obj.getJsonObject("player"));
        return ret;
    }
}
