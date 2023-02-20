package orpheus.client.gui.components;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import net.protocols.ChatMessageListener;
import orpheus.client.ClientAppContext;

/*
ChatProtocol cp = new ChatProtocol(chatServer);
cp.addChatListener(this);
chatServer.setChatProtocol(cp);
 */


/**
 * A widget players can use to message other players, which also displays some
 * output from the program.
 */
public class ChatBox extends JComponent implements ChatMessageListener {
    
    /**
     * allows players to show / hide the chat
     */
    private final JButton toggleButton;

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
     * notified when a message is logged in the chat
     */
    private final List<Consumer<String>> messageListeners;

    public ChatBox(ClientAppContext context) {
        super();
        setLayout(new BorderLayout());
        var cf = context.getComponentFactory();
        
        var top = cf.makePanel();
        top.setLayout(new BorderLayout());
        toggleButton = cf.makeButton("X", this::toggle);
        top.add(toggleButton, BorderLayout.LINE_END);
        top.add(cf.makeLabel("Chat"), BorderLayout.CENTER);
        add(top, BorderLayout.PAGE_START);

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

        messageListeners = new ArrayList<>();
    }

    /**
     * Registers the given listener to be notified of message this user sends.
     * @param listener the listener to register
     */
    public void addMessageListener(Consumer<String> listener) {
        messageListeners.add(listener);
    }

    private void toggle() {
        var visible = body.isVisible();
        toggleButton.setText("O");
        toggleButton.setToolTipText("open chat");
        body.setVisible(false);
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
        messageListeners.forEach((listener) -> listener.accept(message));
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

    @Override
    public void messageReceived(String message) {
        output(message);
    }
}
