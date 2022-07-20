package orpheus.client.gui.components;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

import javax.swing.*;

import net.OrpheusServer;
import net.messages.*;
import net.protocols.ChatMessageListener;
import net.protocols.ChatProtocol;
import users.LocalUser;

public class Chat extends JComponent implements ActionListener, ChatMessageListener {
    private final JTextArea msgs;
    private final JScrollPane box;
    private final JTextField newMsg;
    private final HashMap<String, Consumer<String[]>> CMDS = new HashMap<>();
    private final OrpheusServer chatServer;
    private boolean chatServerOpened;
    
    public Chat(ComponentFactory cf, OrpheusServer chatServer){
        super();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        msgs = cf.makeTextArea("###CHAT###");
        msgs.setColumns(20);
        
        box = new JScrollPane(msgs);
        gbc.weighty = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(box, gbc.clone());
        
        newMsg = new JTextField();
        newMsg.setMaximumSize(new Dimension(Integer.MAX_VALUE, newMsg.getPreferredSize().height));
        newMsg.addActionListener(this);
        gbc.weighty = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(newMsg, gbc.clone());
        
        initCmds();
        
        logLocal("enter '/?' to list commands");
        
        this.chatServer = chatServer;
        chatServerOpened = false;
        
        revalidate();
        repaint();
    }
    
    private void initCmds(){
        addCmd("?", (String[] ss)->listCmds());
        addCmd("connect", (ss)->{
            try {
                String ip = ss[0].split(":")[0];
                int port = Integer.parseInt(ss[0].split(":")[1], 10);
                joinChat(ip, port);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }
    public void addCmd(String cmd, Consumer<String[]> function){
        CMDS.put(cmd.toUpperCase(), function);
    }
    
    //todo make this better, list what cmds do
    public void listCmds(){
        logLocal("COMMANDS: ");
        CMDS.keySet().forEach((cmd)->logLocal("* /" + cmd));
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e){
        String s = newMsg.getText();
        if(s.startsWith("/")){
            //is command
            String[] split = s.toUpperCase().replace("/", "").split(" ");
            String cmd = split[0];
            if(CMDS.containsKey(cmd)){
                CMDS.get(cmd).accept(Arrays.copyOfRange(split, 1, split.length));
            } else {
                logLocal("Invalid command: " + cmd);
            }
        } else {
            log(newMsg.getText());
        }
        newMsg.setText("");
    }
    
    public void logLocal(String msg){
        msgs.setText(msgs.getText() + '\n' + msg);
        SwingUtilities.invokeLater(()->{
            box.getVerticalScrollBar().setValue(box.getVerticalScrollBar().getMaximum());
            box.repaint();
        });
    }
    public void log(String msg){
        logLocal("You: " + msg);
        if(chatServer != null && chatServer.isStarted()){
            ServerMessage sm = new ServerMessage(msg, ServerMessageType.CHAT);
            chatServer.send(sm);
        }
    }
    
    public void openChatServer() {
        if(chatServer != null){
            ChatProtocol cp = new ChatProtocol(chatServer);
            cp.addChatListener(this);
            chatServer.setChatProtocol(cp);
        }
    }
    
    public void joinChat(String ipAddr, int port) throws IOException{
        if(!chatServerOpened){
            openChatServer();
        }
        if(this.chatServer.isStarted()){
            logLocal("Joined chat with " + ipAddr);
            chatServer.send(new ServerMessage(
                LocalUser.getInstance().getName() + " has joined the chat.",
                ServerMessageType.CHAT
            ));
        }
    }

    @Override
    public void messageReceived(String message) {
        logLocal(message);
    }
}
