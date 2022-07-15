package net.connections;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import net.messages.ServerMessage;
import users.AbstractUser;

/**
 * Connections is simply a collection of Connection objects.
 * 
 * @author Matt Crow
 */
public class Connections {
    private final HashMap<AbstractUser, Socket> userToSocket;
    private final HashMap<Socket, Connection> connections;
    
    public Connections(){
        userToSocket = new HashMap<>();
        connections = new HashMap<>();
    }
    
    public final boolean isConnectedTo(AbstractUser client){
        return userToSocket.containsKey(client);
    }
    
    public final boolean isConnectedTo(Socket client){
        return connections.containsKey(client);
    }
    
    public final void connectTo(Socket client) throws IOException{
        if(isConnectedTo(client)){
            connections.get(client).close();
        }
        connections.put(client, new Connection(client));
    }
    
    public final void disconnectFrom(Socket client){
        if(connections.containsKey(client)){
            Connection c = connections.get(client);
            c.close();
            // may need to fire disconnection listeners
            connections.remove(client);
            
            // remove entry from user table
            AbstractUser u = userToSocket.entrySet().stream().filter((e)->{
                return e.getValue().equals(client);
            }).map((e)->e.getKey()).findFirst().orElse(null);
            
            if(u != null){
                userToSocket.remove(u);
            }
        }
    }
    
    public final void disconnectFrom(AbstractUser client){
        if(userToSocket.containsKey(client)){
            disconnectFrom(userToSocket.get(client));
            userToSocket.remove(client);
        }
    }
    
    public final void closeAll(){
        // avoid concurrent modification exception
        AbstractUser[] all = userToSocket.keySet().toArray(new AbstractUser[connections.size()]);
        for(AbstractUser s : all){
            disconnectFrom(s);
        }
    }
    
    public final Connection getConnectionTo(Socket client){
        return connections.get(client);
    }
    
    public final Connection getConnectionTo(AbstractUser user){
        return connections.get(userToSocket.get(user));
    }
    
    public final void broadcast(ServerMessage sm) throws IOException{
        for(Connection conn : connections.values()){
            conn.writeServerMessage(sm);
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

    public void setUser(AbstractUser sender, Socket sendingSocket) {
        userToSocket.put(sender, sendingSocket);
        getConnectionTo(sendingSocket).setRemoteUser(sender);
    }
}
