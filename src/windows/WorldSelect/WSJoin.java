package windows.WorldSelect;

import controllers.Master;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class WSJoin extends SubPage{
    //                                     contains 1-3 digits
    //                                              followed by a period
    private static final String IP_REGEX = "\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}[.]\\d{1,3}";
    // BUG: matches strings starting or ending with more than 3 digits
    
    public WSJoin(Page p) {
        super(p);
        //work in progress
        JTextField ip = new JTextField("enter host address here");
        ip.addActionListener((e)->{
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
    
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter something to test if it is an IP address, then type 'exit' to quit.");
        String ip = "";
        Pattern p = Pattern.compile(IP_REGEX);
        while(!"exit".equalsIgnoreCase(ip)){
            ip = in.nextLine();
            System.out.print(ip + " is ");
            if(!p.matcher(ip).find()){
                System.out.print("not ");
            }
            System.out.println("an IP address");
        }
    }
}
