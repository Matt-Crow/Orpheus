package windows.customize;

import gui.BuildSelect;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import windows.NewPage;

/**
 *
 * @author Matt
 */
public class CustomizeChooseBuild extends NewPage{
    private final BuildSelect buiSel;
    public CustomizeChooseBuild(){
        super();
        setLayout(new BorderLayout());
        
        addBackButton(()->{
            CustomizePage p = new CustomizePage(getHost());
            getHost().switchToPage(p);
            p.switchToSubpage(CustomizePage.MAIN);
        });
        
        add(new JLabel("Select Built to Customize"), BorderLayout.PAGE_START);
        
        buiSel = new BuildSelect();
        buiSel.refreshOptions();
        add(buiSel, BorderLayout.CENTER);
        
        JButton customize = new JButton("Customize this build");
        customize.addActionListener((e)->{
            CustomizeBuild cb = new CustomizeBuild();
            cb.setCustomizing(buiSel.getSelectedBuild());
            getHost().switchToPage(cb);
        });
        add(customize, BorderLayout.PAGE_END);
    }
}
