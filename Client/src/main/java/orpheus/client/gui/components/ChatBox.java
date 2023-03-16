package orpheus.client.gui.components;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import orpheus.client.ClientAppContext;
import orpheus.core.net.chat.ChatMessageSentListener;

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

    /**
     * notified when the user sends a chat message
     */
    private final List<ChatMessageSentListener> messageSentListeners;

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

        messageSentListeners = new ArrayList<>();
    }

    /**
     * Registers the given listener to be notified of message the user sends.
     * @param listener the listener to register
     */
    public void addMessageSentListener(ChatMessageSentListener listener) {
        messageSentListeners.add(listener);
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
        messageSentListeners.forEach((listener) -> listener.chatMessageSent(message));
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
