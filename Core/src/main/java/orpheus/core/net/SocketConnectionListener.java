package orpheus.core.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.UUID;

/**
 * Listens for connections on a network socket,
 * then tells an OrpheusServer about any connections it receives.
 */
public class SocketConnectionListener {
    private final UUID uuid = UUID.randomUUID();
    private final OrpheusServer server;
    private final ServerSocket serverSocket;
    private final Thread connectionListenerThread;

    private static final int USE_ANY_PORT = 0;
    private static final int CONNECTION_TIME_OUT = 3000; // 3 seconds
    

    private SocketConnectionListener(OrpheusServer server, ServerSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
        
        connectionListenerThread = new Thread(this::listenForNewConnections, "SocketConnectionListener(%s)".formatted(uuid));
        connectionListenerThread.start();
    }
    
    /**
     * @param server the server to send connections to
     * @return a connection listener for the given server
     * @throws IOException if a socket cannot be set up
     */
    public static SocketConnectionListener forServer(OrpheusServer server) throws IOException {
        var serverSocket = new ServerSocket(USE_ANY_PORT);
        serverSocket.setSoTimeout(CONNECTION_TIME_OUT);

        return new SocketConnectionListener(server, serverSocket);
    }
    
    private void listenForNewConnections() {
        log("Listening on %s".formatted(getSocketAddress()));
        while (server.isRunning()) {
            try {
                var client = serverSocket.accept();
                
                log("Received connection to %s:%d".formatted(
                    client.getInetAddress().getHostAddress(),
                    client.getPort()
                ));

                server.connectTo(client);
            } catch (SocketTimeoutException ste) {
                // this is not an error, it just means no client has attempted to connect
            } catch (IOException ex) {
                log(ex.toString());
            }
        }

        log("Server is no longer running, so I'll shut down.");

        // Server is done running, so clean up
        try {
            serverSocket.close();
        } catch (IOException ex) {
            log(ex.toString());
        }

        log("Finished shutting down.");
    }

    /**
     * @return the socket address clients can connect to
     */
    public SocketAddress getSocketAddress() {
        var socketAddress = new SocketAddress(
            serverSocket.getInetAddress().getHostAddress(), 
            serverSocket.getLocalPort()
        );
        return socketAddress;
    }

    private void log(String message) {
        System.out.printf("SocketConnectionListener(%s): %s\n", uuid, message);
    }
}
