package gui.pages.worldSelect;

import java.io.IOException;
import net.OrpheusClient;
import net.OrpheusServer;
import net.ServerProvider;
import net.protocols.WaitingRoomClientProtocol;
import net.protocols.WaitingRoomHostProtocol;
import users.LocalUser;

/**
 *
 * @author Matt Crow
 */
public class WSNewMulti extends AbstractWSNewWorld{
    public WSNewMulti(){
        super();
    }
    
    @Override
    public void start(){
        LocalUser.getInstance().loginWindow();
        try{
            OrpheusServer server = new ServerProvider().createHost();
            WaitingRoomHostProtocol hostProtocol = new WaitingRoomHostProtocol(
                server,
                createBattle()
            );
            server.setProtocol(hostProtocol);            
            
            OrpheusClient client = new OrpheusClient(server.getIpAddress(), server.getPort());
            WaitingRoom room = new WaitingRoom();
            WaitingRoomClientProtocol clientProtocol = new WaitingRoomClientProtocol(client, room);
            client.setProtocol(clientProtocol);
            client.start();
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
