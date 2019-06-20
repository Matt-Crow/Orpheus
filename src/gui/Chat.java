package gui;

import controllers.Master;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.ServerMessage;

/*
Not liking how this is turning out as a static class,
will probably change this to a gui component
*/
public class Chat extends JComponent{
    private static final HashMap<String, Consumer<String[]>> CMDS = new HashMap<>();
	
    private final JTextArea msgs;
    private final JScrollPane box;
    private final JTextField newMsg;
    
    
    static{
        initCmds();
    }
    
    public Chat(){
        super();
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        msgs = new JTextArea("###CHAT###");
        msgs.setEditable(false);
        msgs.setWrapStyleWord(true);
        msgs.setLineWrap(true);
        
        box = new JScrollPane(msgs);
        gbc.weighty = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(box, gbc.clone());
        
        newMsg = new JTextField();
        newMsg.setMaximumSize(new Dimension(Integer.MAX_VALUE, newMsg.getPreferredSize().height));
        newMsg.addActionListener((e)->{
            if(!runIfCmd(newMsg.getText())){
                log(newMsg.getText());
            }
            newMsg.setText("");
        });
        gbc.weighty = 1;
        gbc.gridy = 1;
        add(newMsg, gbc.clone());
        
        revalidate();
        repaint();
    }
    
    
    
    
    
    private static void initCmds(){
        addCmd("?", (String[] ss)->listCmds());
        addCmd("connect", (ss)->joinChat(ss[0]));
    }
    
    public static void openChatServer(){
        if(Master.getServer() == null){
            try {
                Master.startServer();
            } catch (IOException ex) {
                //logLocal("Failed to start chat server");
                ex.printStackTrace();
            }
        }
        
        //started successfully
        if(Master.getServer() != null){
            //Master.getServer().setState(OrpheusServerState.WAITING_ROOM);
            //Master.getServer().setReceiverFunction((String s)->logLocal(s));
            /*
            try {
                //logLocal("Initialized chat server on " + InetAddress.getLocalHost().getHostAddress());
                //logLocal("Have other people use the \'/connect " + Master.getServer().getIpAddr() + "\' command (without the quote marks) to connect.");
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }*/
        }
    }
    
    public static void joinChat(String ipAddr){
        if(Master.getServer() == null){
            openChatServer();
        }
        //need to double check to make sure it was started successfully,
        //so an else statement won't work
        if(Master.getServer() != null){
            Master.getServer().connect(ipAddr);
            //Master.getServer().setState(OrpheusServerState.WAITING_ROOM);
            //Master.getServer().setReceiverFunction((String s)->logLocal(s));
            //logLocal("Joined chat with " + ipAddr);
        }
    }
    
    /**
     * Checks to see if the given string is a command,
     * if so, evaluates it, if not, 
     * @param s the string to evaluate
     * @return whether or not the string is a command
     */
    private static boolean runIfCmd(String s){
        boolean isCmd = s.startsWith("/");
        
        if(isCmd){
            String[] split = s.toUpperCase().replace("/", "").split(" ");
            String cmd = split[0];
            if(CMDS.containsKey(cmd)){
                CMDS.get(cmd).accept(Arrays.copyOfRange(split, 1, split.length));
            } else {
                //logLocal("Invalid command: " + cmd);
            }
        }
        
        return isCmd;
    }
    
    public static void addCmd(String cmd, Consumer<String[]> function){
        CMDS.put(cmd.toUpperCase(), function);
    }
    
    //todo make this better, list what cmds do
    public static void listCmds(){
        //logLocal("COMMANDS: ");
        //CMDS.keySet().forEach((cmd)->logLocal("* /" + cmd));
    }
	/*
    public static void logLocal(String msg){
        msgs.setText(msgs.getText() + '\n' + msg);
	}*/
    
    public void logLocal(String msg){
        msgs.setText(msgs.getText() + '\n' + msg);
    }
    
	public static void log(String msg){
        //logLocal("You: " + msg);
        if(Master.getServer() != null){
            System.out.println("sending message...");
            ServerMessage sm = new ServerMessage(Master.getServer().getIpAddr(), msg, ServerMessage.CHAT_MESSAGE);
            Master.getServer().send(sm);
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
        Chat.openChatServer();
        f.revalidate();
        f.repaint();
        //Chat.joinChat(JOptionPane.showInputDialog("enter IP address to connect to: "));
    }
}
