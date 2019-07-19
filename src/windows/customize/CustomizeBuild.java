package windows.customize;

import customizables.Build;
import gui.CustomizableSelector;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class CustomizeBuild extends SubPage{
    private CustomizableSelector charClassSel;
    private CustomizableSelector[] acts;
    private CustomizableSelector[] pass;
    
    public CustomizeBuild(Page p){
        super(p);
    }

    public void setCustomizing(Build selectedBuild) {
         
    }
}
