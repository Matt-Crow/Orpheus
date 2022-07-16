package orpheus.client.gui.pages.customize;

import orpheus.client.gui.components.BuildSelect;
import java.awt.BorderLayout;
import javax.swing.JButton;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.PageController;

/**
 *
 * @author Matt
 */
public class CustomizeChooseBuild extends Page{
    private final BuildSelect buiSel;
    public CustomizeChooseBuild(PageController host){
        super(host);
        BorderLayout b = new BorderLayout();
        b.setVgap(30);
        setLayout(b);
        
        addBackButton(new CustomizeMain(host));
        
        buiSel = new BuildSelect();
        buiSel.refreshOptions();
        add(buiSel, BorderLayout.CENTER);
        
        JButton customize = new JButton("Customize this build");
        customize.addActionListener((e)->{
            CustomizeBuild cb = new CustomizeBuild(host);
            cb.setCustomizing(buiSel.getSelectedBuild());
            getHost().switchToPage(cb);
        });
        add(customize, BorderLayout.PAGE_END);
    }
}
