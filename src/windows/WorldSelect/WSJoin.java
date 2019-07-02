package windows.WorldSelect;

import controllers.Master;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class WSJoin extends SubPage{
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
}
