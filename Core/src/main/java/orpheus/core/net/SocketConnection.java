package orpheus.core.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * A connection to either a client or a server running Orpheus on another process
 */
public class SocketConnection extends Connection {
    private final Socket clientSocket;    
    private final BufferedReader fromClient;
    private final BufferedWriter toClient;

    private SocketConnection(Socket s, OutputStream os, InputStream is){
        clientSocket = s;
        toClient = new BufferedWriter(new OutputStreamWriter(os));
        fromClient = new BufferedReader(new InputStreamReader(is));
    }

    /**
     * Attempts to establish a Connection to the given remote address
     * @param socketAddress the address to connect to
     * @return a connection to the given address
     * @throws IOException if any exceptions occur when connection to the given address
     */
    public static Connection forRemote(SocketAddress socketAddress) throws IOException {
        var socket = new Socket(socketAddress.getAddress(), socketAddress.getPort());
        return SocketConnection.forSocket(socket);
    }

    /**
     * Attempts to establish a Connection with the given Socket
     * @param socket the Socket to connect to
     * @return a Connection around the given Socket
     * @throws IOException if any exceptions occur when establishing the connection
     */
    public static Connection forSocket(Socket socket) throws IOException {
        return new SocketConnection(socket, socket.getOutputStream(), socket.getInputStream());
    }

    @Override
    public final void writeServerMessage(Message sm) throws IOException{        
        // do I need to make sure the JsonString contains no newlines?
        toClient.write(sm.toJsonString());
        toClient.write('\n');
        toClient.flush();
    }

    @Override
    public final Message readServerMessage() throws IOException{
        // This works, so I guess toJsonString excapes newlines or something?
        return Message.deserializeJson(fromClient.readLine());
    }

    public void close(){
        try {
            toClient.close();
        } catch (IOException ex) {
            System.err.println("couldn't close output");
            ex.printStackTrace();
        }
        try {
            fromClient.close();
        } catch (IOException ex) {
            System.err.println("couldn't close input");
            ex.printStackTrace();
        }
        try {
            clientSocket.close();
        } catch (IOException ex) {
            System.err.println("couldn't close socket");
            ex.printStackTrace();
        }
    }

    @Override
    public String toString(){
        var remoteUser = getRemoteUser();
        String userName = (remoteUser == null) ? "---" : remoteUser.getName();
        return String.format("Connection to %s:%d %s", 
            clientSocket.getInetAddress().getHostAddress(),
            clientSocket.getPort(),
            userName
        );
    }
}
