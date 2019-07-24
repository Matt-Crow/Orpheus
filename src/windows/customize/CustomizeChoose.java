package windows.customize;

import customizables.AbstractCustomizable;
import customizables.CustomizableType;
import customizables.actives.AbstractActive;
import customizables.characterClass.CharacterClass;
import customizables.passives.AbstractPassive;
import gui.CustomizableSelector;
import java.awt.BorderLayout;
import javax.swing.JButton;
import windows.NewPage;

/**
 *
 * @author Matt
 */
public class CustomizeChoose extends NewPage{
    public CustomizeChoose(CustomizableType type){
        super();
        setLayout(new BorderLayout());
        
        AbstractCustomizable[] options = new AbstractCustomizable[]{};
        switch(type){
            case ACTIVE:
                options = AbstractActive.getAll();
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
