package orpheus.client.gui.pages.worldselect;

import java.io.IOException;
import net.OrpheusClient;
import net.OrpheusServer;
import net.ServerProvider;
import orpheus.client.WaitingRoomClientProtocol;
import net.protocols.WaitingRoomHostProtocol;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.PageController;
import users.LocalUser;

/**
 *
 * @author Matt Crow
 */
public class WSNewMulti extends AbstractWSNewWorld{
    public WSNewMulti(PageController host, ComponentFactory cf){
        super(host, cf);
    }
    
    @Override
    public void start(){
        LocalUser.getInstance().loginWindow();
        try{
            OrpheusServer server = new ServerProvider().createHost();
            WaitingRoomHostProtocol hostProtocol = new WaitingRoomHostProtocol(
                server,
                createGame()
            );
            server.setProtocol(hostProtocol);   
            server.start();
            
            OrpheusClient client = new OrpheusClient(server.getIpAddress(), server.getPort());
            WaitingRoom room = new WaitingRoom(getHost(), getComponentFactory());
            WaitingRoomClientProtocol clientProtocol = new WaitingRoomClientProtocol(client, room);
            client.setProtocol(clientProtocol);
            client.start();
            room.setBackEnd(clientProtocol);
            room.getChat().logLocal(String.format(
                "Have other people use the /connect %s command to connect.", 
                server.getConnectionString()
            ));
            getHost().switchToPage(room);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
