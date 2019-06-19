package gui;

import controllers.Master;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import net.OrpheusServerState;

public class Chat {
    private static final JPanel content;
	private static final JTextArea msgs;
    private static final JScrollPane box;
    private static final JTextField newMsg;
	
    static{
        content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        
        msgs = new JTextArea("###CHAT###");
        msgs.setEditable(false);
        box = new JScrollPane(msgs);
        gbc.weighty = 4;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(box, gbc.clone());
        
        newMsg = new JTextField();
        newMsg.setMaximumSize(new Dimension(Integer.MAX_VALUE, newMsg.getPreferredSize().height));
        newMsg.addActionListener((e)->{
            log(newMsg.getText());
            newMsg.setText("");
        });
        gbc.weighty = 1;
        gbc.gridy = 1;
        content.add(newMsg, gbc.clone());
        
        content.revalidate();
        content.repaint();
    }
    public static void openChatServer(){
        if(Master.getServer() == null){
            try {
                Master.startServer();
                Master.getServer().setState(OrpheusServerState.WAITING_ROOM);
                Master.getServer().setReceiverFunction((String s)->logLocal(s));
                logLocal("Initialized chat server on " + InetAddress.getLocalHost().getHostAddress());
            } catch (IOException ex) {
                logLocal("Failed to start chat server");
                ex.printStackTrace();
            }
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
            logLocal("Joined chat with " + ipAddr);
        }
    }
    
    /*
    public static void initServer(){
        
        if(chatServer == null){
            try {
                chatServer = new Server(5000);
                logLocal("Initialized chat server on " + InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static void listenToServer(String ipAddr){
        if(chatClient != null){
            chatClient.terminate();
        }else{
            chatClient = new Client(ipAddr, 5000, (String s)->logLocal(s));
            logLocal("Initialized chat client on " + ipAddr);
        }
    }*/
	
    public static void logLocal(String msg){
        msgs.setText(msgs.getText() + '\n' + msg);
	}
    
	public static void log(String msg){
        logLocal("You: " + msg);
        if(Master.getServer() != null){
            System.out.println("sending message...");
            Master.getServer().send(Master.getServer().getIpAddr() + " " + msg);
        }
	}
	public static void addTo(JPanel j){
        j.add(content);
	}
    
    public static void main(String[] args){
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1, 1));
        f.setContentPane(p);
        
        Chat.addTo(p);
        Chat.openChatServer();
        Chat.joinChat(JOptionPane.showInputDialog("enter IP address to connect to: "));
    }
}
