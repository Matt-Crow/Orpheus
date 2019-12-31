package windows.customize;

import controllers.Master;
import customizables.AbstractCustomizable;
import customizables.CustomizableType;
import customizables.DataSet;
import customizables.actives.AbstractActive;
import customizables.characterClass.CharacterClass;
import customizables.passives.AbstractPassive;
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
                options = AbstractPassive.getAll();
                break;
            case CHARACTER_CLASS:
                options = CharacterClass.getAll();
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
