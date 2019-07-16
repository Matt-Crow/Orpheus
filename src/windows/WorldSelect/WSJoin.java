package windows.WorldSelect;

import controllers.Master;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class WSJoin extends SubPage{
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
    
    public WSJoin(Page p) {
        super(p);
        //work in progress
        JTextArea msgs = new JTextArea("Messages will appear here!");
        msgs.setLineWrap(true);
        
        JScrollPane scrolly = new JScrollPane(msgs);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrolly);
        
        JTextField ip = new JTextField("enter host address here");
        ip.addActionListener((e)->{
            if(isIpAddr(ip.getText())){
                System.out.println("OK!");
            } else {
                System.out.println("Not OK!");
                return;
            }
            
            if(Master.getServer() == null){
                try {
                    Master.startServer();
                } catch (IOException ex) {
                    ip.setText("Failed to start local server");
                    ex.printStackTrace();
                }
            }
            if(Master.getServer() != null){
                Master.loginWindow();
                getHostingPage().switchToSubpage(WorldSelectPage.WAIT);
                if(getHostingPage().getCurrentSubPage() instanceof WSWaitForPlayers){
                    ((WSWaitForPlayers)getHostingPage().getCurrentSubPage()).joinServer(ip.getText()).joinTeam2(Master.getUser());
                }
            }
        });
        add(ip);
    }
    
    private static boolean isIpAddr(String s){
        return REGEX.matcher(s).find();
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
