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
    CONTROL_PRESSED("control pressed"),
    START_WORLD("start"),
    SERVER_SHUTDOWN("END OF LINE"),

    /**
     * When received by the client, it will deserialize the message body into a
     * world graph. The server should not respond to this message.
     */
    WORLD("world"),
    
    /**
     * When received by the server, it will respond with a list of available 
     * waiting rooms. When received by a client, they should decode the waiting
     * rooms listed within.
     */
    LIST_WAITING_ROOMS("list waiting rooms"),

    /**
     * When received by the server, it will respond with the port of a new 
     * waiting room. When received by a client, they should connect to the port
     * contained in the message.
     */
    NEW_WAITING_ROOM("new waiting room"),

    /**
     * Indicates the socket this was received from is about to close, and 
     * should therefore stop being used.
     */
    CLOSE_CONNECTION("close connection");
    
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
