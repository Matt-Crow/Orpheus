package orpheus.client.gui.components;

import java.awt.BorderLayout;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.PageController;
import orpheus.client.gui.pages.worldselect.WaitingRoomPage;
import orpheus.client.protocols.WaitingRoomClientProtocol;
import orpheus.core.net.SocketAddress;

/**
 * A component which allows users to specify the IP address of a hub to connect
 * to.
 */
public class HubForm extends JComponent {

    /**
     * the context this is executing under
     */
    private final ClientAppContext context;
    
    /**
     * error & success messages
     */
    private final JTextArea messages;

    /**
     * where the user enters the IP address of the server they want to connect
     * to - not validated
     */
    private final JTextField addressInput;
    // while I could validate this, it isn't worth the time in a personal project

    /**
     * where the user enters the port of the server they want to connect to
     */
    private final JFormattedTextField portInput;

    /**
     * temp
     */
    private final PageController host;

    public HubForm(ClientAppContext context, String title, PageController host) {
        this.context = context;
        this.host = host;

        setLayout(new BorderLayout());

        var cf = context.getComponentFactory();
        var center = cf.makePanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));

        center.add(cf.makeLabel(title));

        // todo maybe have error message area in the main UI
        messages = cf.makeTextArea("Messages will appear here!\n");
        center.add(cf.makeVerticalScrollAround(messages));

        var addressRow = cf.makePanel();
        addressRow.add(cf.makeLabel("IP Address"));
        addressInput = new JTextField("xxx.yyy.zzz.ttt");
        addressInput.setColumns(20);
        addressRow.add(addressInput);
        center.add(addressRow);

        var portRow = cf.makePanel();
        portRow.add(cf.makeLabel("Port"));
        portInput = new JFormattedTextField(NumberFormat.getIntegerInstance());
        portInput.setColumns(20);
        portRow.add(portInput);
        center.add(portRow);

        add(center, BorderLayout.CENTER);

        add(cf.makeButton("Join", this::submit), BorderLayout.PAGE_END);
    }

    private void submit() {
        var port = portInput.getValue();
        if (port == null || !(port instanceof Number)) {
            messages.append("Please enter a number for port.");
            return;
        }

        var address = addressInput.getText();
        var portNumber = ((Number)port).intValue();
        var socketAddress = new SocketAddress(address, portNumber);

        try {
            doOldConnection(socketAddress);
        } catch (IOException ex) {
            messages.append("Failed to connect to server: \n");
            messages.append(ex.getMessage() + '\n');
        }
        messages.append("Connected!\n");
    }

    private void doOldConnection(SocketAddress address) throws IOException {
        context.showLoginWindow(); // ask annonymous users to log in
        var user = context.getLoggedInUser();

        var client = new net.OrpheusClient(user, address);
        var waitingRoomPage = new WaitingRoomPage(context, host);
        var protocol = new WaitingRoomClientProtocol(client, waitingRoomPage);
        waitingRoomPage.setBackEnd(protocol);
        client.setMessageHandler(Optional.of(protocol));
        waitingRoomPage.getChat().handleChatMessagesFor(client);
        
        client.start();
        host.switchToPage(waitingRoomPage);
    }
}
