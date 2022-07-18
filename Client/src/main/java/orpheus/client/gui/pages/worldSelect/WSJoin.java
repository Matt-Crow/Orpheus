package orpheus.client.gui.pages.worldSelect;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import users.LocalUser;
import orpheus.client.gui.pages.Page;
import java.text.NumberFormat;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import net.OrpheusClient;
import orpheus.client.WaitingRoomClientProtocol;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.PageController;

/**
 * Checking for IPv4 is currently disabled, as
 * I need to allow for IPv6 connections.
 * 
 * @author Matt
 */
public class WSJoin extends Page{
    private static final String IPV4_REGEX = 
        "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    /*
    Or I could just take it from the internet. 
    https://www.techiedelight.com/validate-ip-address-java/
    https://www.tutorialspoint.com/java/java_regular_expressions
    
    WHAT IT MEANS:
    ^       : this is the start of the line
    (stuff) : consider stuff as a group
    [a-b]   : matches something in the range of a to b
    a | b   : a or b
    stuff?  : 0 or 1 occurences
    \\.     : I assume this means "is '.'"
    $       : this is the end of the line
    */
    private static final Pattern REGEX = Pattern.compile(IPV4_REGEX);
    
    private final JTextArea msgs;
    private final JTextField ip;
    private final JFormattedTextField port;
    
    public WSJoin(PageController host, ComponentFactory cf) {
        super(host, cf);
        
        addBackButton(new WSMain(host, cf));
        
        setLayout(new GridLayout(1, 2));
        
        //left side
        msgs = new JTextArea("Messages will appear here!\n");
        msgs.setLineWrap(true);
        msgs.setWrapStyleWord(true);
        msgs.setEditable(false);
        
        JScrollPane scrolly = new JScrollPane(msgs);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrolly);
        
        //right side
        ip = new JTextField("enter host address here");
        
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(ip);
        
        port = new JFormattedTextField(NumberFormat.getIntegerInstance());
        right.add(port);
        
        right.add(cf.makeButton("Join", ()->{
            if(!isIpAddr(ip.getText())){
                msgs.append('"' + ip.getText() + "\" doesn't look like an IP address to me. \n");
            } else if(port.getValue() == null || !(port.getValue() instanceof Number)){
                msgs.append("please specify a port");
            } else {
                int p = ((Number)port.getValue()).intValue();
                join(ip.getText(), p);
            }
        }));
        
        add(right);
    }
    
    private static boolean isIpAddr(String s){
        return REGEX.matcher(s).find();
    }
    
    private void join(String ipAddr, int port){
        try {
            LocalUser.getInstance().loginWindow(); // set username
            msgs.append("Attempting to connect to " + ipAddr + "...\n");
            OrpheusClient connection = new OrpheusClient(ipAddr, port);
            WaitingRoom wait = new WaitingRoom(getHost(), getComponentFactory());
            WaitingRoomClientProtocol protocol = new WaitingRoomClientProtocol(connection, wait);
            wait.setBackEnd(protocol);
            connection.setProtocol(protocol);
            connection.start();
            msgs.append("success!\n");
            getHost().switchToPage(wait);
            //wait.joinServer(ipAddr);
        } catch (IOException ex) {
            msgs.append(ex.getMessage() + '\n');
            msgs.append(Arrays.toString(ex.getStackTrace()) + '\n');
        }
    }
    
    public static void main(String[] args){
        try (Scanner in = new Scanner(System.in)) {
            System.out.println("Enter something to test if it is an IP address, then type 'exit' to quit.");
            String ip = "";
            while(!"exit".equalsIgnoreCase(ip)){
                ip = in.nextLine();
                System.out.print(ip + " is ");
                if(!isIpAddr(ip)){
                    System.out.print("not ");
                }
                System.out.println("an IP address");
            }
        }
    }
}
