package windows.customize;

import gui.BuildSelect;
import java.awt.BorderLayout;
import javax.swing.JButton;
import windows.Page;

/**
 *
 * @author Matt
 */
public class CustomizeChooseBuild extends Page{
    private final BuildSelect buiSel;
    public CustomizeChooseBuild(){
        super();
        BorderLayout b = new BorderLayout();
        b.setVgap(30);
        setLayout(b);
        
        addBackButton(new CustomizeMain());
        
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
