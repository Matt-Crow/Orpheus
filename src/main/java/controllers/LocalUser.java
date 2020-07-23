package controllers;

import net.OrpheusServer;

/**
 *
 * @author Matt
 */
public class LocalUser extends AbstractUser {
    private String remotePlayerId;
    
    private static LocalUser instance;
    
    private LocalUser(String name){
        super(name);
        if(instance != null){
            throw new ExceptionInInitializerError("LocalUser is a singleton: Use LocalUser.getInstance() instead of explicitely calling the constructor");
        }
        remotePlayerId = null;
    }
    private LocalUser(){
        this(AbstractUser.DEFAULT_NAME);
    }
    
    public static LocalUser getInstance(){
        if(instance == null){
            instance = new LocalUser();
        }
        return instance;
    }
    
    public final void setRemotePlayerId(String id){
        remotePlayerId = id;
    }
    
    public final String getRemotePlayerId(){
        return remotePlayerId;
    }

    @Override
    public String getIpAddress() {
        return OrpheusServer.getInstance().getIpAddr();
    }
}
