package net.connections;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * Connections is simply a collection of Connection objects.
 * 
 * @author Matt Crow
 */
public class Connections {
    private final HashMap<Socket, Connection> connections;
    
    public Connections(){
        connections = new HashMap<>();
    }
    
    public final void connectTo(Socket client) throws IOException{
        if(connections.containsKey(client)){
            connections.get(client).close();
        }
        connections.put(client, new Connection(client));
    }
    
    public final void disconnectFrom(Socket client){
        if(connections.containsKey(client)){
            connections.get(client).close();
            // may need to fire disconnection listeners
            connections.remove(client);
        }
    }
    
    public final void closeAll(){
        // avoid concurrent modification exception
        Socket[] all = connections.keySet().toArray(new Socket[connections.size()]);
        for(Socket s : all){
            disconnectFrom(s);
        }
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Connections:");
        connections.forEach((sock, conn)->{
            sb.append(String.format("\n* %s", conn.toString()));
        });
        return sb.toString();
    }
}