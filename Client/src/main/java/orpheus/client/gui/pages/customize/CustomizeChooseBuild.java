package orpheus.client.gui.pages.customize;

import orpheus.client.gui.components.BuildSelect;
import java.awt.BorderLayout;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.PageController;

/**
 *
 * @author Matt
 */
public class CustomizeChooseBuild extends Page{
    private final BuildSelect buiSel;
    public CustomizeChooseBuild(PageController host, ComponentFactory cf){
        super(host, cf);
        BorderLayout b = new BorderLayout();
        b.setVgap(30);
        setLayout(b);
        
        addBackButton(new CustomizeMain(host, cf));
        
        buiSel = new BuildSelect(cf);
        buiSel.refreshOptions();
        add(buiSel, BorderLayout.CENTER);
        
        add(cf.makeButton("Customize this build", ()->{
            CustomizeBuild cb = new CustomizeBuild(host, cf);
            cb.setCustomizing(buiSel.getSelectedBuild());
            getHost().switchToPage(cb);
        }), BorderLayout.PAGE_END);
    }
}
