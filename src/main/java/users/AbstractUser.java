package users;

import java.net.Socket;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonSerialable;
import serialization.JsonUtil;

/**
 *
 * @author Matt
 */
public abstract class AbstractUser implements JsonSerialable {
    private String userName;
    
    public static final String DEFAULT_NAME = "name not set";
    
    public AbstractUser(String name){
        userName = name;
    }
    public AbstractUser(){
        this(DEFAULT_NAME);
    }
    
    public void setName(String newName){
        userName = newName;
    }
    public String getName(){
        return userName;
    }
    
    public abstract Socket getSocket();
    
    /**
     * Serializes this user into JSON. Note that both LocalUsers
     * and RemoteUsers are serialized the same way
     * 
     * This may serialize the IP address of this
     * user, iff the IP address has been set. I
     * need this for the WaitingRoomClientProtocol
     * so it can receive the IP address when deserializing
     * the World Init message.
     * 
     * @return the User, serialized into JSON 
     */
    @Override
    public JsonObject serializeJson(){
        JsonObjectBuilder objBuild = Json.createObjectBuilder();
        objBuild.add("type", "user");
        objBuild.add("name", userName);
        try{
            objBuild.add("ip address", getSocket().getInetAddress().getHostAddress());
            objBuild.add("port", getSocket().getPort());
        } catch (NullPointerException ex){
            
        }
        return objBuild.build();
    }
    
    /**
     * de-serializes a JsonObject of a RemoteUser.
     * 
     * Note that this means a LocalUser serialized on one computer
     * will be de-serialized as a RemoteUser on another.
     * 
     * @param obj
     * @return 
     */
    public static AbstractUser deserializeJson(JsonObject obj){
        JsonUtil.verify(obj, "type");
        JsonUtil.verify(obj, "name");
        if(!obj.getString("type").equals("user")){
            throw new JsonException("not a user");
        }
        
        RemoteUser u = new RemoteUser(obj.getString("name"));
        
        return u;
    }
}
