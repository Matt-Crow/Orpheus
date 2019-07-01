package windows;

import gui.Style;
import java.awt.Component;
import javax.swing.JPanel;

/**
 * Subpages are rendered by the CardLayout used by the Page class.
 * The Page acts as a controller for its SubPages, choosing which ones to render.
 * @author Matt
 */
public class SubPage extends JPanel{
    private final Page inPage;
    
    public SubPage(Page p){
        super();
        inPage = p;
        
        Style.applyStyling(this);
    }
    
    public Page getHostingPage(){
        return inPage;
    }
    
    @Override
	public Component add(Component j){
		super.add(j);
        Style.applyStyling(j);
		return j;
	}
    
    //this is the supermethod called by all variations of add
    @Override
    public void addImpl(Component comp, Object constraints, int index){
        super.addImpl(comp, constraints, index);
        Style.applyStyling(comp);
    }
}
