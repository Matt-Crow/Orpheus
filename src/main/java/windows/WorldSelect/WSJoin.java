package windows.WorldSelect;

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
import windows.Page;

/**
 * IPV4 regex disabled for now
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
    
    public WSJoin() {
        super();
        
        addBackButton(new WSMain());
        
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
        ip.addActionListener((e)->{
            //if(isIpAddr(ip.getText())){
                join(ip.getText());
            //} else {
            //    msgs.append('"' + ip.getText() + "\" doesn't look like an IP address to me. \n");
            //}
        });
        
        JPanel right = new JPanel();
        right.add(ip);
        
        add(right);
    }
    
    private static boolean isIpAddr(String s){
        return REGEX.matcher(s).find();
    }
    
    private void join(String ipAddr){
        try {
            LocalUser.getInstance().loginWindow(); // set username
            msgs.append("Attempting to connect to " + ipAddr + "...\n");
            ClientWaitingRoom wait = new ClientWaitingRoom(ipAddr);
            wait.startProtocol();
            msgs.append("success!\n");
            getHost().switchToPage(wait);
            //wait.joinServer(ipAddr);
        } catch (IOException ex) {
            msgs.append(ex.getMessage() + '\n');
            msgs.append(Arrays.toString(ex.getStackTrace()) + '\n');
        }
    }
    
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
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
