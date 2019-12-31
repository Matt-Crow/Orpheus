package windows.customize;

import controllers.Master;
import customizables.AbstractCustomizable;
import customizables.CustomizableType;
import customizables.DataSet;
import gui.CustomizableSelector;
import java.awt.BorderLayout;
import javax.swing.JButton;
import windows.Page;

/**
 *
 * @author Matt
 */
public class CustomizeChoose extends Page{
    public CustomizeChoose(CustomizableType type){
        super();
        addBackButton(new CustomizeMain());
        
        setLayout(new BorderLayout());
        
        DataSet ds = Master.getDataSet();
        AbstractCustomizable[] options = new AbstractCustomizable[]{};
        switch(type){
            case ACTIVE:
                options = ds.getAllActives();
                break;
            case PASSIVE:
                options = ds.getAllPassives();
                break;
            case CHARACTER_CLASS:
                options = ds.getAllCharacterClasses();
                break;
            default:
                System.err.println("No options for " + type + " in CustomizeChoose");
        }
        
        CustomizableSelector chooser = new CustomizableSelector("Choose " + type.toString() + " to customize", options);
        add(chooser, BorderLayout.CENTER);
        
        JButton customize = new JButton("Customize");
        customize.addActionListener((e)->{
            CustomizeCustomize cc = new CustomizeCustomize();
            cc.setCustomizing(chooser.getSelected());
            getHost().switchToPage(cc);
        });
        add(customize, BorderLayout.PAGE_END);
    }
}
