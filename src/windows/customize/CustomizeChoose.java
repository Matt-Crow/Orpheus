package windows.customize;

import customizables.AbstractCustomizable;
import customizables.CustomizableType;
import customizables.actives.AbstractActive;
import customizables.characterClass.CharacterClass;
import customizables.passives.AbstractPassive;
import gui.CustomizableSelector;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class CustomizeChoose extends SubPage{
    private final CustomizableSelector chooser;
    private final CustomizableType t;
    public CustomizeChoose(Page p, CustomizableType type){
        super(p);
        setLayout(new BorderLayout());
        
        t = type;
        
        AbstractCustomizable[] options = new AbstractCustomizable[]{};
        
        JLabel title = new JLabel("Choose " + type.toString() + " to customize");
        add(title, BorderLayout.PAGE_START);
        
        
        
        chooser = new CustomizableSelector("what?", options);
        add(chooser, BorderLayout.CENTER);
        
        JButton customize = new JButton("Customize");
        customize.addActionListener((e)->{
            
        });
        add(customize, BorderLayout.PAGE_END);
    }

    @Override
    public void switchedFromThis() {}

    @Override
    public void switchedToThis() {
        AbstractCustomizable[] options = null;
        switch(t){
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
                System.err.println("No options for " + t + " in CustomizeChoose");
        }
        chooser.setOptions(options);
    }
}
