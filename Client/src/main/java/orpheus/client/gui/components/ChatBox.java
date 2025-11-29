package orpheus.client.gui.components;

import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import orpheus.client.ClientAppContext;
import orpheus.core.net.*;
import orpheus.core.utils.EventEmitter;

/**
 * A widget players can use to message other players, which also displays some
 * output from the program.
 */
public class ChatBox extends JComponent {

    /**
     * can be toggled visible / invisible
     */
    private final JPanel body;

    /**
     * messages sent from / received by the player
     */
    private final JTextArea messages;

    /**
     * where the player types new messages
     */
    private final JTextField inputBox;

    /**
     * allows the user to view previous messages
     */
    private final JScrollPane scrolly;

    private final EventEmitter<String> chatMessageSentEvent = new EventEmitter<>();


    public ChatBox(ClientAppContext context) {
        super();
        setLayout(new BorderLayout());
        var cf = context.getComponentFactory();

        body = cf.makePanel();
        body.setLayout(new BorderLayout());
        add(body, BorderLayout.CENTER);

        messages = cf.makeTextArea("");
        messages.setColumns(20);
        scrolly = cf.makeVerticalScrollAround(messages);
        body.add(scrolly, BorderLayout.CENTER);

        inputBox = new JTextField();
        inputBox.setToolTipText("type your message here");
        inputBox.addActionListener((ActionEvent e) -> sendInput());
        body.add(inputBox, BorderLayout.PAGE_END);
    }

    /**
     * Configures this to handle sending and receiving chat messages for the given client
     * @param client the client to handle chat messages for
     */
    public void handleChatMessagesFor(OrpheusClient client) {
        chatMessageSentEvent.addEventListener(client::sendChatMessage);
        client.setChatMessageHandler(this::outputChatMessage);
    }

    private void outputChatMessage(ChatMessage chatMessage) {
        var senderName = chatMessage.getSender().getName();
        var message = chatMessage.getMessage();
        output(String.format("(%s): %s", senderName, message));
    }

    private void sendInput() {
        send(inputBox.getText());
        inputBox.setText("");
    }

    /**
     * Sends the given message to each player on the server.
     * @param message the message to send.
     */
    public void send(String message) {
        chatMessageSentEvent.emitEvent(message);
        output("You: " + message);
    }

    /**
     * Outputs the given message to the player - not sent to the server.
     * @param message the message to write.
     */
    public void output(String message) {
        messages.setText(messages.getText() + '\n' + message);
        SwingUtilities.invokeLater(() -> {
            // scroll down
            var bar = scrolly.getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
            scrolly.repaint();
        });
    }
}
