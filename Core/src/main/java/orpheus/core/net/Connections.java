package orpheus.core.net;

import java.io.IOException;
import java.net.Socket;
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

    public final boolean isConnectedTo(Connection connection) {
        return connections.contains(connection);
    }
    
    // TODO encapsulate dependency on Socket
    public final boolean isConnectedTo(Socket client){
        var result = connections.stream()
        .anyMatch(c -> c.getClientSocket().equals(client));
        return result;
    }
    
    // TODO encapsulate dependency on Socket
    public final void connectTo(Socket client) throws IOException{
        if(isConnectedTo(client)){
            getConnectionTo(client).close();
        }
        connections.add(new Connection(client));
    }
    
    // TODO encapsulate dependency on Socket
    public final void disconnectFrom(Socket client){
        if (isConnectedTo(client)) {
            disconnectFrom(getConnectionTo(client));
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
    
    public final Connection getConnectionTo(Socket client){
        return connections.stream()
            .filter(c -> client.equals(c.getClientSocket()))
            .findFirst()
            .get();
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

    // TODO encapsulate dependency on Socket
    public void setUser(User sender, Socket sendingSocket) {
        getConnectionTo(sendingSocket).setRemoteUser(sender);
    }
}
