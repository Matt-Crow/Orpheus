package gui;

import controllers.Master;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import javax.swing.*;
import net.ServerMessage;
import net.ServerMessageType;

public class Chat extends JComponent implements ActionListener{
    private final JTextArea msgs;
    private final JScrollPane box;
    private final JTextField newMsg;
    private final HashMap<String, Consumer<String[]>> CMDS = new HashMap<>();
    private final Consumer<ServerMessage> receiver = (sm)->{
        logLocal(String.format("(%s): %s", sm.getSender().getName(), sm.getBody()));
    };
    
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
        
        revalidate();
        repaint();
    }
    
    private void initCmds(){
        addCmd("?", (String[] ss)->listCmds());
        addCmd("connect", (ss)->joinChat(ss[0]));
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
    }
    public void log(String msg){
        logLocal("You: " + msg);
        if(Master.getServer() != null){
            System.out.println("sending message...");
            ServerMessage sm = new ServerMessage(msg, ServerMessageType.CHAT);
            Master.getServer().send(sm);
        }
	}
    
    
    public void openChatServer(){
        if(Master.getServer() == null){
            try {
                Master.startServer();
            } catch (IOException ex) {
                logLocal("Failed to start chat server");
                ex.printStackTrace();
            }
        }
        
        //started successfully
        if(Master.getServer() != null){
            Master.getServer().addReceiver(ServerMessageType.CHAT, receiver);
            logLocal("Initialized chat server on " + Master.getServer().getIpAddr());
            logLocal("Have other people use the \'/connect " + Master.getServer().getIpAddr() + "\' command (without the quote marks) to connect.");
        }
    }
    
    public void joinChat(String ipAddr){
        if(Master.getServer() == null){
            openChatServer();
        }
        //need to double check to make sure it was started successfully,
        //so an else statement won't work
        if(Master.getServer() != null){
            Master.getServer().connect(ipAddr);
            Master.getServer().addReceiver(ServerMessageType.CHAT, receiver);
            logLocal("Joined chat with " + ipAddr);
            log(Master.getServer().getIpAddr() + " has joined the chat.");
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
        c.openChatServer();
        f.revalidate();
        f.repaint();
    }
}
