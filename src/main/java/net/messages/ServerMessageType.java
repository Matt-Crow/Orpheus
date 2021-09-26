package net.messages;

import java.util.Arrays;

/**
 *
 * @author Matt
 */
public enum ServerMessageType {
    CHAT("chat"),
    PLAYER_JOINED("player joined"),
    PLAYER_LEFT("player left"),
    WAITING_ROOM_INIT("waiting room init"),
    WAITING_ROOM_UPDATE("waiting room update"),
    REQUEST_PLAYER_DATA("request player data"),
    PLAYER_DATA("player data"),
    NOTIFY_IDS("notify ids"), //used by WSWaitForPlayers to tell Users what their Team's and Player's IDs are on the host server
    WORLD_INIT("world init"),
    WORLD_UPDATE("world update"),
    CONTROL_PRESSED("control pressed"),
    START_WORLD("start"),
    SERVER_SHUTDOWN("END OF LINE"),
    OTHER("other");
    
    private final String name;
    
    private ServerMessageType(String s){
        name = s;
    }
    
    public static ServerMessageType fromString(String s){
        ServerMessageType ret = null;
        //Lambdas are so cool
        ret = Arrays
            .stream(values())
            .filter((smt)->smt.toString().equalsIgnoreCase(s))
            .findFirst()
            .get();
        return ret;
    }
    
    @Override
    public String toString(){
        return name;
    }
}
