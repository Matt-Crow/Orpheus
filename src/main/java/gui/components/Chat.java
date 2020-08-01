package gui.components;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import javax.swing.*;
import net.OrpheusServer;
import net.messages.ServerMessage;
import net.messages.ServerMessagePacket;
import net.messages.ServerMessageType;
import net.protocols.ChatProtocol;
import users.LocalUser;

public class Chat extends JComponent implements ActionListener{
    private final JTextArea msgs;
    private final JScrollPane box;
    private final JTextField newMsg;
    private final HashMap<String, Consumer<String[]>> CMDS = new HashMap<>();
    private boolean chatServerOpened;
    
    public Chat(){
        super();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        //need to set how many rows and columns it displays, not the size
        msgs = new JTextArea("###CHAT###", 4, 20);
        msgs.setEditable(false);
        msgs.setWrapStyleWord(true);
        msgs.setLineWrap(true);
        
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
        
        chatServerOpened = false;
        
        revalidate();
        repaint();
    }
    
    private void initCmds(){
        addCmd("?", (String[] ss)->listCmds());
        addCmd("connect", (ss)->{
            try {
                joinChat(ss[0]);
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
        if(OrpheusServer.getInstance().isStarted()){
            ServerMessage sm = new ServerMessage(msg, ServerMessageType.CHAT);
            OrpheusServer.getInstance().send(sm);
        }
	}
    
    
    // maybe move this stuff
    public void openChatServer() throws IOException{
        if(!chatServerOpened){
            new ChatProtocol(this).applyProtocol();
            chatServerOpened = true;
        }
    }
    
    public void joinChat(String ipAddr) throws IOException{
        if(!chatServerOpened){
            openChatServer();
        }
        OrpheusServer server = OrpheusServer.getInstance();
        if(server.isStarted()){
            try {
                server.connect(ipAddr);
                logLocal("Joined chat with " + ipAddr);
                server.send(new ServerMessage(
                    LocalUser.getInstance().getName() + " has joined the chat.",
                    ServerMessageType.CHAT
                ));
            } catch (IOException ex) {
                logLocal(ex.getMessage());
            }
            
        }
    }
    
    public static void main(String[] args){
        JFrame f = new JFrame();
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 1));
        f.setContentPane(p);
        p.setBackground(Color.red);
        
        Chat c = new Chat();
        p.add(c);
        try {
            c.openChatServer();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        f.revalidate();
        f.repaint();
    }
}
