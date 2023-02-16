package orpheus.server.connections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.Socket;

import javax.json.Json;

import orpheus.core.net.messages.Message;
import orpheus.core.net.messages.MessageType;

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

    private Connection(Socket other, BufferedReader from, BufferedWriter to) {
        this.other = other;
        this.from = from;
        this.to = to;
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
        var message = Message.fromJson(json);
        return message;
    }

    public void close() throws IOException {
        send(new Message(MessageType.CLOSE_CONNECTION));
        from.close();
        to.close();
        other.close();
    }
}
