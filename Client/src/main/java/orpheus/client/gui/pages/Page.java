
package orpheus.client.gui.pages;

import java.awt.Component;
import java.util.function.Supplier;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import orpheus.client.gui.components.ComponentFactory;

/**
 * Each Page is used as a complete view of the GUI.
 * 
 * Each Page consists of 2 sections:
 * - A menu bar along the top of the screen,
 * - and a main content section which takes up the rest.
 * 
 * Use this component like a regular JPanel, but use addMenuItem to add items to
 * the menu.
 * 
 * @author Matt Crow
 */
public class Page extends JPanel{
    private final PageController host;
    private final ComponentFactory components;
    private final JMenuBar menuBar;
    
    public Page(PageController host, ComponentFactory components){
        this.host = host;
        this.components = components;
        menuBar = new JMenuBar();
    }
    
    /**
     * Used to get the PageController this is
     * contained in. Use getHost().switchToPage(...)
     * to switch between pages.
     * 
     * @return the Main Window that is rendering Orpheus
     */
    public PageController getHost(){
        return host;
    }
    
    /**
     * @return the component factory providing components to this
     */
    public ComponentFactory getComponentFactory(){
        return components;
    }
    
    /**
     * @return the menu of actions associated with this page
     */
    public JMenuBar getMenuBar(){
        return menuBar;
    }
    
    /**
     * Adds a button to the menu bar which, when clicked, will redirect
     * the user to the given Page
     * 
     * @param text the text to display on the back button.
     * @param p supplies the page this button should link to
     * @return this
     */
    public Page addBackButton(String text, Supplier<Page> p){
        JButton b = components.makeButton(text, ()->{
            if(p != null){
                /*
                supplier is more memory efficient than passing a Page, as now
                the page is only instantiated upon clicking the button
                */
                getHost().switchToPage(p.get());
            }
        });
        addMenuItem(b);
        return this;
    }
    
    /**
     * Adds a button to the menu bar which, when clicked, will redirect
     * the user to the given Page
     * 
     * @param p supplies the page this button should link to
     * @return this
     */
    public Page addBackButton(Supplier<Page> p){
        return addBackButton("Back", p);
    }
    
    /**
     * @param c the component to add
     * @return this
     */
    public Page addMenuItem(Component c){
        if(c == null){
            throw new NullPointerException();
        }
        menuBar.add(c);
        return this;
    }
    
    /**
     * called by PageController upon moving to this Page
     */
    public void enteredPage(){
        
    }
    
    /**
     * called by PageController upon switching from this Page to another.
     * Subclasses should override this method if they require tear-down behavior.
     */
    public void leavingPage(){
        
    }
}
