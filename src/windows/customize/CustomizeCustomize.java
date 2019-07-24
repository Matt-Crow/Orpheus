package windows.customize;

import customizables.AbstractCustomizable;
import customizables.actives.AbstractActive;
import customizables.characterClass.CharacterClass;
import customizables.passives.AbstractPassive;
import gui.ActiveCustomizer;
import gui.CharacterClassCustomizer;
import gui.PassiveCustomizer;
import java.awt.GridLayout;
import javax.swing.JPanel;
import windows.NewPage;

/**
 * The part where you actually customize things
 * @author Matt
 */
public class CustomizeCustomize extends NewPage{
    private final JPanel customArea;
    private AbstractCustomizable newCustom;
    public CustomizeCustomize(){
        super();
        addBackButton(new CustomizeMain());
        setLayout(new GridLayout(1, 1));
        
        customArea = new JPanel();
        add(customArea);
        
        newCustom = null;
    }
    
    public void setCustomizing(AbstractCustomizable ac){
        customArea.removeAll();
        newCustom = ac;
        switch(newCustom.upgradableType){
            case ACTIVE:
                add(new ActiveCustomizer((AbstractActive) newCustom));
                break;
            case PASSIVE:
                add(new PassiveCustomizer((AbstractPassive) newCustom));
                break;
            case CHARACTER_CLASS:
                add(new CharacterClassCustomizer((CharacterClass)newCustom));
                break;
            default:
                throw new IllegalArgumentException("Need to add option to customize " + newCustom.upgradableType);
        }
    }
}
