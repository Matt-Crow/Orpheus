package orpheus.core.net.messages;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.BiConsumer;

import orpheus.core.net.Connection;

/**
 * This class listens for messages sent through a connection, forwarding them to
 * a server.
 * 
 * @author Matt Crow
 */
public class MessageListener {
    private final Connection listeningTo;
    private final BiConsumer<Socket, Message> messageConsumer;
    private volatile boolean isListening;
    
    public MessageListener(Connection listeningTo, BiConsumer<Socket, Message> messageConsumer){
        this.listeningTo = listeningTo;
        this.messageConsumer = messageConsumer;
        isListening = false;
    }
    
    public final void startListening(){
        //do I need to store this somewhere?
        isListening = true;
        Thread t = new Thread(){
            @Override
            public void run(){
                awaitMessages();
            }
        };
        t.start();
    }
    
    private void awaitMessages(){
        while(isListening){
            attemptRead();
        }
        
    }
    
    private void attemptRead(){
        try {
            read();
        } catch(EOFException connectionDone){
            isListening = false;
        } catch (SocketException sock){
            isListening = false;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void read() throws IOException {
        var fromClient = listeningTo.readServerMessage();
        ServerMessageType type = fromClient.getType();
        if(type == ServerMessageType.SERVER_SHUTDOWN || type == ServerMessageType.PLAYER_LEFT){
            isListening = false;
        } else {
            messageConsumer.accept(listeningTo.getClientSocket(), fromClient);
        }
    }
}
