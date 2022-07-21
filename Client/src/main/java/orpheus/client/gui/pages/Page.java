
package orpheus.client.gui.pages;

import gui.graphics.CustomColors;
import java.awt.BorderLayout;
import java.awt.Component;
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
        super.setLayout(new BorderLayout());
        
        this.host = host;
        this.components = components;
        menuBar = new JMenuBar();
        
        setBackground(CustomColors.black);
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
     * the user to the given Page (p), then run the given runnable.
     * 
     * @param text the text to display on the back button.
     * @param p the page this button should link to
     * @param onGoBack the runnable to run after switching pages
     * @return this
     */
    public Page addBackButton(String text, Page p, Runnable onGoBack){
        JButton b = components.makeButton(text, ()->{
            if(p != null){
                getHost().switchToPage(p);
            }
            onGoBack.run();
        });
        addMenuItem(b);
        return this;
    }
    
    /**
     * Adds a button to the menu bar which, when clicked, will redirect
     * the user to the given Page (p)
     * 
     * @param text the text to display on the back button.
     * @param p the page this button should link to
     * @return this
     */
    public Page addBackButton(String text, Page p){
        return addBackButton(text, p, ()->{});
    }
    
    /**
     * Adds a button to the menu bar which, when clicked, will redirect
     * the user to the given Page (p)
     * 
     * @param p the page this button should link to
     * @return this
     */
    public Page addBackButton(Page p){
        return addBackButton("Back", p);
    }
    
    /**
     * Adds a button to the menu bar which, when clicked, will redirect
     * the user to the given Page (p), then run the given runnable.
     * 
     * @param p the page this button should link to
     * @param r the runnable to run after switching pages
     * @return this
     */
    public Page addBackButton(Page p, Runnable r){
        return addBackButton("Back", p, r);
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
}
