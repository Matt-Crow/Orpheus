package orpheus.core.net.messages;

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
    START_WORLD("start"),
    SERVER_SHUTDOWN("END OF LINE"),

    /**
     * Used to serialize control presses via the command design pattern so the
     * server can execute them.
     */
    CONTROL_PRESSED("control pressed"),

    /**
     * When received by the client, it will deserialize the message body into a
     * world graph. The server should not respond to this message.
     */
    WORLD("world"),

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
