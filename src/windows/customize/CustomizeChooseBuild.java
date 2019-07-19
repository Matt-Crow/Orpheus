package windows.customize;

import gui.BuildSelect;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class CustomizeChooseBuild extends SubPage{
    private final BuildSelect buiSel;
    public CustomizeChooseBuild(Page p){
        super(p);
        setLayout(new BorderLayout());
        
        add(new JLabel("Select Built to Customize"), BorderLayout.PAGE_START);
        
        buiSel = new BuildSelect();
        add(buiSel, BorderLayout.CENTER);
        
        JButton customize = new JButton("Customize this build");
        customize.addActionListener((e)->{
            getHostingPage().switchToSubpage(CustomizePage.CUSTOM_BUILD);
            SubPage page = getHostingPage().getCurrentSubPage();
            if(page instanceof CustomizeBuild){
                ((CustomizeBuild)page).setCustomizing(buiSel.getSelectedBuild());
            }
        });
        add(customize, BorderLayout.PAGE_END);
    }
    
    @Override
    public void switchedToThis(){
        super.switchedToThis();
        buiSel.refreshOptions();
    }
}
