package orpheus.client.gui.pages.worldselect;

import java.io.IOException;

import net.OrpheusClient;
import net.OrpheusServer;
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
        getContext().showLoginWindow(); // ask annonymous users to log in
        try{
            OrpheusServer server = new ServerProvider().createHost();
            WaitingRoomHostProtocol hostProtocol = new WaitingRoomHostProtocol(
                server,
                createGame(),
                getContext().getDataSet()
            );
            server.setProtocol(hostProtocol);   
            server.start();
            
            OrpheusClient client = new OrpheusClient(
                getContext().getLoggedInUser(),
                server.getIpAddress(),
                server.getPort()
            );
            WaitingRoom room = new WaitingRoom(getContext(), getHost());
            WaitingRoomClientProtocol clientProtocol = new WaitingRoomClientProtocol(client, room);
            client.setProtocol(clientProtocol);
            client.start();

            getContext().setClient(client);
            
            room.setBackEnd(clientProtocol);

            // set up chat protocol
            var chatProtocol = new ClientChatProtocol(
                getContext().getLoggedInUser(),
                client, 
                room.getChat()
            );
            client.setChatProtocol(chatProtocol);

            room.getChat().output(String.format(
                "Server started on %s", 
                server.getConnectionString()
            ));
            getHost().switchToPage(room);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
