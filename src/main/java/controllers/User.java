package controllers;

import world.AbstractWorld;
import entities.HumanPlayer;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.swing.JOptionPane;
import net.OrpheusServer;
import serialization.JsonSerialable;
import serialization.JsonUtil;

/**
 * 
 * A User is meant to replace the TruePlayer class.
 * The class represents a real person- not just a AbstractPlayer entity
 controlled by AI.
 * 
 * 
 * Replace this with Singleton LocalUser and non-singleton RemoteUser
 * 
 * @author Matt Crow
 */
public final class User implements JsonSerialable{
    private String userName;
    private String ipAddr;
    private HumanPlayer player;
    
    private String remotePlayerId;
    
    private static final User user = new User();
    
    public static final String DEFAULT_NAME = "name not set";
    
    public User(String name, String ip){
        userName = name;
        ipAddr = ip;
        player = null;
    }
    
    public User(String name){
        this(name, OrpheusServer.getInstance().getIpAddr());
    }
    
    public User(){
        this(DEFAULT_NAME);
    }
    
    public static void loginWindow(){
        if(user.getName().equals(User.DEFAULT_NAME)){
            user.setName(JOptionPane.showInputDialog("Enter a name: "));
        }
    }
    
    public User setName(String s){
        userName = s;
        return this;
    }
    public String getName(){
        return userName;
    }
    
    public User setRemotePlayerId(String id){
        remotePlayerId = id;
        return this;
    }
    public String getRemotePlayerId(){
        return remotePlayerId;
    }
    
    public User setIpAddress(String ip){
        ipAddr = ip;
        return this;
    }
    public String getIpAddress(){
        return ipAddr;
    }
    
    /**
     * Used to re-associate this User with a AbstractPlayer
     * received from a serialized world
     * 
     * I can only get rid of this by removing getPlayer from this
     * 
     * @param w
     * @return 
     */
    public User linkToRemotePlayerInWorld(AbstractWorld w){
        setPlayer(
            (HumanPlayer)w
                .getPlayerTeam()
                .getMemberById(remotePlayerId)
        );
        return this;
    }
    
    /**
     * Creates a new AbstractPlayer entity, and associates it with this user
 note that this does not call TruePlayer's init method
     * @return this, for chaining purposes
     */
    public User initPlayer(){
        player = new HumanPlayer(userName);
        return this;
    }
    //used for receiving remote player
    public User setPlayer(HumanPlayer p){
        player = p;
        return this;
    }
    public HumanPlayer getPlayer(){
        if(player == null){
            initPlayer();
        }
        return player;
    }    
    
    @Override
    public boolean equals(Object o){
        return o != null && o instanceof User && ((User)o).ipAddr.equals(ipAddr);
    }

    @Override
    public int hashCode() {
        return ipAddr.hashCode();
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
        User ret = new User(obj.getString("name"), obj.getString("ip address"));
        //ret.player = AbstractPlayer.deserializeJson(obj.getJsonObject("player"));
        return ret;
    }
}
