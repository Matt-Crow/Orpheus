package net.messages;

import java.io.EOFException;
import java.io.IOException;
import java.util.function.Consumer;
import net.connections.Connection;

/**
 * This class listens for messages sent through a connection, forwarding them to
 * a server.
 * 
 * @author Matt Crow
 */
public class MessageListener {
    private final Connection listeningTo;
    private final Consumer<ServerMessagePacket> messageConsumer;
    private volatile boolean isListening;
    
    public MessageListener(Connection listeningTo, Consumer<ServerMessagePacket> messageConsumer){
        this.listeningTo = listeningTo;
        this.messageConsumer = messageConsumer;
        isListening = false;
    }
    
    public final void startListening(){
        //do I need to store this somewhere?
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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void read() throws IOException {
        ServerMessagePacket fromClient = listeningTo.readServerMessage();
        if(fromClient.getMessage().getType() == ServerMessageType.SERVER_SHUTDOWN){
            isListening = false;
        } else {
            messageConsumer.accept(fromClient);
        }
    }
}
