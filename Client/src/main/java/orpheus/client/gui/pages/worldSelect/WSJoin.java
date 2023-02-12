package orpheus.client.gui.pages.worldselect;

import java.awt.BorderLayout;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import net.OrpheusClient;
import orpheus.client.ClientAppContext;
import orpheus.client.WaitingRoomClientProtocol;
import orpheus.client.gui.pages.Page;
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
    
    public WSJoin(ClientAppContext context, PageController host) {
        super(context, host);
        var cf = context.getComponentFactory();
        addBackButton(()-> new WSMain(context, host));
        
        setLayout(new BorderLayout());
        
        JPanel center = cf.makePanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        add(center, BorderLayout.CENTER);
        
        msgs = cf.makeTextArea("Messages will appear here!\n");        
        JScrollPane scrolly = new JScrollPane(msgs);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        center.add(scrolly);
        
        // input form
        JPanel addressRow = cf.makePanel();
        center.add(addressRow);
        addressRow.add(cf.makeLabel("IP Address"));
        ip = new JTextField("xxx.yyy.zzz.ttt");
        ip.setColumns(20);
        addressRow.add(ip);
        
        JPanel portRow = cf.makePanel();
        center.add(portRow);
        portRow.add(cf.makeLabel("Port"));
        port = new JFormattedTextField(NumberFormat.getIntegerInstance());
        port.setColumns(20);
        portRow.add(port);
        
        add(cf.makeSpaceAround(cf.makeButton("Join", this::joinButtonClicked)), BorderLayout.PAGE_END);
    }
    
    private void joinButtonClicked(){
        if(!isIpAddr(ip.getText())){
            msgs.append('"' + ip.getText() + "\" doesn't look like an IP address to me. \n");
        } else if(port.getValue() == null || !(port.getValue() instanceof Number)){
            msgs.append("please specify a port");
        } else {
            int p = ((Number)port.getValue()).intValue();
            join(ip.getText(), p);
        }
    }
    
    private static boolean isIpAddr(String s){
        return REGEX.matcher(s).find();
    }
    
    private void join(String ipAddr, int port){
        getContext().showLoginWindow(); // ask annonymous users to log in
        msgs.append("Attempting to connect to " + ipAddr + "...\n");
        try {
            OrpheusClient connection = new OrpheusClient(getContext().getLoggedInUser(), ipAddr, port);
            WaitingRoom wait = new WaitingRoom(getContext(), getHost());
            WaitingRoomClientProtocol protocol = new WaitingRoomClientProtocol(connection, wait);
            wait.setBackEnd(protocol);
            connection.setProtocol(protocol);
            connection.start();
            msgs.append("success!\n");
            getHost().switchToPage(wait);
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
