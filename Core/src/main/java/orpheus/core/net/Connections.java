package orpheus.core.net;

import java.io.IOException;
import java.util.ArrayList;

import orpheus.core.users.User;

/**
 * Connections is simply a collection of Connection objects.
 * 
 * @author Matt Crow
 */
public class Connections {
    private final ArrayList<Connection> connections = new ArrayList<>();
    
    
    public final boolean isConnectedTo(User client){
        var result = connections.stream()
            .anyMatch(c -> client.equals(c.getRemoteUser()));
        return result;
    }

    private final boolean isConnectedTo(Connection connection) {
        return connections.contains(connection);
    }
    
    public final void connectTo(Connection connection) {
        if (!isConnectedTo(connection)) {
            connections.add(connection);
        }
    }
    
    public final void disconnectFrom(Connection connection) {
        if (!isConnectedTo(connection)) {
            return;
        }

        connection.close();
        connections.remove(connection);

        // may need to fire disconnection listeners
    }
    
    public final void closeAll(){
        // avoid concurrent modification exception
        var shallowCopy = new ArrayList<Connection>(connections);
        for (var connection : shallowCopy) {
            disconnectFrom(connection);
        }
    }
    
    public final Connection getConnectionTo(User user){
        return connections.stream()
            .filter(c -> user.equals(c.getRemoteUser()))
            .findFirst()
            .get();
    }
    
    public final void broadcast(Message sm) throws IOException{
        for(Connection conn : connections){
            conn.writeServerMessage(sm);
        }
    }

    public void sendToAllExcept(Message message, User user) throws IOException {
        for (var conn : connections) {
            if (!conn.getRemoteUser().equals(user)) {
                conn.writeServerMessage(message);
            }
        }
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Connections:");
        connections.forEach(conn -> sb.append(String.format("\n* %s", conn.toString())));
        return sb.toString();
    }
}
