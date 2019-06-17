package windows.WorldSelect;

import gui.Style;
import windows.SubPage;
import java.awt.GridLayout;
import javax.swing.JButton;
import windows.Page;

/**
 * The Main sub page for the world select canvas.
 * @author Matt Crow
 */
public class WSMain extends SubPage{
    public WSMain(Page host){
        super(host);
        setLayout(new GridLayout(1, 3));
        
        add(soloButton());
        add(newMultiButton());
        add(joinMultiButton());
        Style.applyStyling(this);
    }
    
    private JButton soloButton(){
        JButton solo = new JButton("Play a game offline");
        Style.applyStyling(solo);
        solo.addActionListener((e)->{
            getHostingPage().switchToSubpage(WorldSelectCanvas.SOLO);
        });
        return solo;
    }
    
    private JButton newMultiButton(){
        JButton newMulti = new JButton("Host a multiplayer game");
        Style.applyStyling(newMulti);
        newMulti.addActionListener((e)->{
            getHostingPage().switchToSubpage(WorldSelectCanvas.NEW_MULTIPLAYER);
        });
        return newMulti;
    }
    
    private JButton joinMultiButton(){
        JButton joinMulti = new JButton("Join a multiplayer game");
        Style.applyStyling(joinMulti);
        joinMulti.addActionListener((e)->{
            //getHostingPage().switchToSubpage(WorldSelectCanvas.JOIN_MULTIPLAYER);
            getHostingPage().switchToSubpage(WorldSelectCanvas.WAIT);
            SubPage sp = getHostingPage().getCurrentSubPage();
            if(sp instanceof WSWaitForPlayers){
                ((WSWaitForPlayers)sp).joinServer();
            }
        });
        return joinMulti;
    }
}
