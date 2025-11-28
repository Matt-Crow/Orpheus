package orpheus.core.net.connections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javax.json.Json;

import orpheus.core.net.messages.*;

/**
 * a connection between an OrpheusServer & OrpheusClient
 */
public class Connection {
    
    /**
     * a socket to the other machine
     */
    private final Socket other;

    /**
     * contains messages from the other machine
     */
    private final BufferedReader from;

    /**
     * contains messages to the other machine
     */
    private final BufferedWriter to;

    /**
     * notifies these upon receiving a message
     */
    private final List<BiConsumer<Connection, Message>> messageListeners;

    private final Thread listenerThread;

    /**
     * whether this should continue listening for messages
     */
    private boolean listening = true;


    private Connection(Socket other, BufferedReader from, BufferedWriter to) {
        this.other = other;
        this.from = from;
        this.to = to;
        messageListeners = new ArrayList<>();
        listenerThread = new Thread(this::listen, String.format("Orpheus Connection to %s:%d", other.getInetAddress(), other.getPort()));
        listenerThread.start();
    }

    /**
     * sets up IO channels with the given socket
     * @param other the socket to connect to
     * @return a connection to the given socket
     * @throws IOException if errors occur while opening a stream on the socket
     */
    public static Connection connect(Socket other) throws IOException {
        var from = new BufferedReader(new InputStreamReader(other.getInputStream()));
        var to = new BufferedWriter(new OutputStreamWriter(other.getOutputStream()));
        var connection = new Connection(other, from, to);
        return connection;
    }

    private void listen() {
        try {
            while (listening) {
                var message = receive();
                notifyListeners(message);
            }
        } catch(IOException ex) {
            // sadly, it doesn't look like there's a better way of handling this
            if (ex.getMessage().equals("Stream closed")) {
                //System.out.println("stream closed: stopping");
                listening = false;
            } else {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Registers the given listener to receive messages received from this 
     * connection.
     * @param listener the listener to register.
     */
    public void addMessageListener(BiConsumer<Connection, Message> listener) {
        messageListeners.add(listener);
    }

    /**
     * sends a message to the other machine
     * @param message the message to send
     * @throws IOException if an error occurs while writing the message
     */
    public void send(Message message) throws IOException {
        to.write(message.toJson().toString());
        to.newLine();
        to.flush();
    }

    public Message receive() throws IOException {
        var line = from.readLine();
        var json = Json.createReader(new StringReader(line)).readObject();
        var message = Message.deserializeJson(json);
        return message;
    }

    public void close() throws IOException {
        send(new Message(ServerMessageType.CLOSE_CONNECTION));
        listening = false;
        from.close();
        to.close();
        other.close();
        listenerThread.interrupt();
    }

    private void notifyListeners(Message message) {
        messageListeners.forEach((listener) -> listener.accept(this, message));
    }
}
