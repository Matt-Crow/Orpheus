package orpheus.server.connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * listens for connections to a socket in a new thread and closes the socket 
 * when it is terminated
 */
public class ConnectionListenerThread {
    private final ServerSocket socket;
    private final Thread thread;
    private boolean running;

    private ConnectionListenerThread(ServerSocket socket) {
        this.socket = socket;
        this.running = true;
        this.thread = new Thread(this::listen, "Orpheus Connection Listener");
    }

    /**
     * starts listening for connections in another thread
     * @param socket the socket to listen for connections to
     * @return the spawned thread
     */
    public static ConnectionListenerThread spawn(ServerSocket socket) {
        var t = new ConnectionListenerThread(socket);
        t.thread.start();
        return t;
    }

    private void listen() {
        Socket client;
        while (running) {
            try {
                client = socket.accept();
                System.out.printf("Connected to %s\n", client.toString());
                client.close();
            } catch (SocketTimeoutException ex) {
                // not an error: it just means no connections received
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            socket.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void terminate() {
        running = false; // allows thread to exit
    }
}
