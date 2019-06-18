package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import net.Client;
import net.Server;

public class Chat {
    private static final JPanel content;
	private static final JTextArea msgs;
    private static final JScrollPane box;
    private static final JTextField newMsg;
    
    private static Server chatServer = null;
    private static Client chatClient = null;
	
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
    
    public static void initServer(){
        if(chatServer == null){
            chatServer = new Server(5000);
            try {
                logLocal("Initialized chat server on " + InetAddress.getLocalHost().getHostAddress());
            } catch (UnknownHostException ex) {
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
    }
	
    public static void logLocal(String msg){
        msgs.setText(msgs.getText() + '\n' + msg);
	}
    
	public static void log(String msg){
        logLocal(msg);
        if(chatServer != null){
            System.out.println("sending message...");
            chatServer.send(msg);
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
        Chat.initServer();
        Chat.listenToServer(JOptionPane.showInputDialog("enter IP address to connect to: "));
    }
}
