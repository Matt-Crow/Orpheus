package orpheus.server.connections;

import java.io.IOException;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * maintains a list of connections so they can be closed on server shutdown
 */
public class Connections {
    
    /**
     * the client connections this is maintaining
     */
    private final HashSet<Connection> connections;

    public Connections() {
        connections = new HashSet<>();
    }

    /**
     * begins managing the given connection
     * @param connection the connection to manage
     */
    public void add(Connection connection) {
        connections.add(connection);
    }

    /**
     * closes all connections contained herein
     */
    public void closeAll() {
        // avoid concurrent modification
        var all = connections.stream().collect(Collectors.toList());
        all.stream().forEach(this::close);
    }

    public void close(Connection connection) {
        if (!connections.contains(connection)) {
            throw new UnsupportedOperationException("This cannot close a connection this does not manage.");
        }

        try {
            connection.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        connections.remove(connection);
    }
}
