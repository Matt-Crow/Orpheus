package orpheus.tests;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.components.Chat;
import net.OrpheusServer;
import net.ServerProvider;
import orpheus.client.gui.components.ComponentFactory;
import util.Settings;

public class ChatTester {
    @Test
    public void chatCanOpenServer() throws IOException{
        ServerProvider sp = new ServerProvider();
        OrpheusServer server = sp.createHost();
        Chat c = new Chat(new ClientAppContext(new Settings(), new ComponentFactory()), server);

        c.openChatServer();
    }
}
