package orpheus.client.gui.pages.worldselect;

import java.io.IOException;
import java.util.Optional;

import net.OrpheusClient;
import net.OrpheusServer;
import net.protocols.WaitingRoomHostProtocol;
import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.PageController;
import orpheus.client.protocols.WaitingRoomClientProtocol;

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

        try{
            var server = new OrpheusServer();
            server.start();
            server.setMessageHandler(Optional.of(new WaitingRoomHostProtocol(
                server,
                createGame(),
                context.getSpecificationResolver()
            )));
            
            var user = context.getLoggedInUser();
            var client = new OrpheusClient(user, server.getSocketAddress());
            client.start();
            var room = new WaitingRoomPage(context, getHost());
            var clientProtocol = new WaitingRoomClientProtocol(client, room);
            room.setBackEnd(clientProtocol);
            client.setMessageHandler(Optional.of(clientProtocol));
            context.setClient(client);
            room.getChat().handleChatMessagesFor(client);
            room.getChat().output(String.format(
                "Server started on %s", 
                server.getSocketAddress().toString()
            ));
            getHost().switchToPage(room);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
