package orpheus.core.net;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import orpheus.core.users.User;

/**
 * Connections is simply a collection of Connection objects.
 * 
 * @author Matt Crow
 */
public class Connections {
    private final HashMap<User, Socket> userToSocket;
    private final HashMap<Socket, Connection> connections;
    
    public Connections(){
        userToSocket = new HashMap<>();
        connections = new HashMap<>();
    }
    
    public final boolean isConnectedTo(User client){
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
            User u = userToSocket.entrySet().stream().filter((e)->{
                return e.getValue().equals(client);
            }).map((e)->e.getKey()).findFirst().orElse(null);
            
            if(u != null){
                userToSocket.remove(u);
            }
        }
    }
    
    public final void closeAll(){
        // avoid concurrent modification exception
        User[] all = userToSocket.keySet().toArray(new User[connections.size()]);
        for(User s : all){
            disconnectFrom(userToSocket.get(s));
            userToSocket.remove(s);
        }
    }
    
    public final Connection getConnectionTo(Socket client){
        return connections.get(client);
    }
    
    public final Connection getConnectionTo(User user){
        return connections.get(userToSocket.get(user));
    }
    
    public final void broadcast(Message sm) throws IOException{
        for(Connection conn : connections.values()){
            conn.writeServerMessage(sm);
        }
    }

    public void sendToAllExcept(Message message, User user) throws IOException {
        for (var conn : connections.values()) {
            if (!conn.getRemoteUser().equals(user)) {
                conn.writeServerMessage(message);
            }
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

    public void setUser(User sender, Socket sendingSocket) {
        userToSocket.put(sender, sendingSocket);
        getConnectionTo(sendingSocket).setRemoteUser(sender);
    }
}
