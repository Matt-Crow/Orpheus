package controllers;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import net.OrpheusServer;
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
        userName = DEFAULT_NAME;
    }
    
    public void setName(String newName){
        userName = newName;
    }
    public String getName(){
        return userName;
    }
    
    public abstract String getIpAddress();
    
    /**
     * Serializes this user into JSON. Note that both LocalUsers
     * and RemoteUsers are serialized the same way
     * 
     * @return the User, serialized into JSON 
     */
    @Override
    public JsonObject serializeJson(){
        JsonObjectBuilder objBuild = Json.createObjectBuilder();
        objBuild.add("type", "user");
        objBuild.add("name", userName);
        objBuild.add("ip address", getIpAddress());
        return objBuild.build();
    }
    
    /**
     * de-serializes a JsonObject of a user.
     * If the ip address contained therein is the same as this
     * computer's OrpheusServer, returns the LocalUser instance.
     * Otherwise, returns a new RemoteUser.
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
        JsonUtil.verify(obj, "ip address");
        if(!obj.getString("type").equals("user")){
            throw new JsonException("not a user");
        }
        
        AbstractUser ret = null;
        if(obj.getString("ip address").equals(OrpheusServer.getInstance().getIpAddr())){
            // is this user
            ret = LocalUser.getInstance();
        } else {
            ret = new RemoteUser(obj.getString("name"), obj.getString("ip address"));
        }
        
        return ret;
    }
}
