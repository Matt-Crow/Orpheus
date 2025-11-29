package orpheus.client.gui.pages.worldselect;

import java.io.IOException;
import java.util.Optional;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.PageController;
import orpheus.client.protocols.WaitingRoomClientProtocol;
import orpheus.core.net.*;
import orpheus.core.net.protocols.WaitingRoomHostProtocol;

/**
 *
 * @author Matt Crow
 */
public class WSNewMulti extends AbstractWSNewWorld{
    public WSNewMulti(ClientAppContext context, PageController host){
        super(context, host);
    }
    
    @Override
    public void start(){
        var context = getContext();
        context.showLoginWindow(); // ask annonymous users to log in

        var server = new OrpheusServer();
        server.setMessageHandler(Optional.of(new WaitingRoomHostProtocol(
            server,
            createGame(),
            context.getSpecificationResolver()
        )));
        
        var user = context.getLoggedInUser();
        try{
            var socketConnectionListener = SocketConnectionListener.forServer(server);
            var client = new OrpheusClient(user, SocketConnection.forRemote(socketConnectionListener.getSocketAddress()));
            
            var room = new WaitingRoomPage(context, getHost());
            var clientProtocol = new WaitingRoomClientProtocol(client, room);
            room.setBackEnd(clientProtocol);
            client.setMessageHandler(Optional.of(clientProtocol));
            room.getChat().handleChatMessagesFor(client);
            room.getChat().output(String.format(
                "Server started on %s", 
                socketConnectionListener.getSocketAddress().toString()
            ));
            getHost().switchToPage(room);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
