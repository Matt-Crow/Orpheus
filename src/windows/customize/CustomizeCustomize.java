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
import windows.Page;
import windows.SubPage;

/**
 * The part where you actually customize things
 * @author Matt
 */
public class CustomizeCustomize extends SubPage{
    private final JPanel customArea;
    private AbstractCustomizable newCustom;
    public CustomizeCustomize(Page p){
        super(p);
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
    
    @Override
    public void switchedFromThis(){
        customArea.removeAll();
        newCustom = null;
    }
}
