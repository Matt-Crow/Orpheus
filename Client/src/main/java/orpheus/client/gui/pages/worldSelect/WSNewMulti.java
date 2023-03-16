package orpheus.client.gui.pages.worldselect;

import java.io.IOException;

import net.ServerProvider;
import net.protocols.WaitingRoomHostProtocol;
import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.PageController;
import orpheus.client.protocols.ClientChatProtocol;
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

        var sp = new ServerProvider();

        try{
            var server = sp.createHost();
            server.setProtocol(new WaitingRoomHostProtocol(
                server,
                createGame(),
                context.getDataSet()
            ));
            
            var user = context.getLoggedInUser();
            var client = sp.createClient(user, server.getSocketAddress());
            var room = new WaitingRoom(context, getHost());
            var clientProtocol = new WaitingRoomClientProtocol(client, room);
            room.setBackEnd(clientProtocol);
            client.setProtocol(clientProtocol);
            context.setClient(client);

            // set up chat protocol
            var chat = room.getChat();
            client.setChatProtocol(new ClientChatProtocol(user, client, chat));

            chat.output(String.format(
                "Server started on %s", 
                server.getSocketAddress().toString()
            ));
            getHost().switchToPage(room);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
