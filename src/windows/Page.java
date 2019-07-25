
package windows;

import controllers.MainWindow;
import graphics.CustomColors;
import gui.Style;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Each Page is used as a view to
 * render the game.
 * 
 * Each Page consists of 2 sections:
 * A menu bar along the top of the screen,
 * and a main content section which takes up
 * the rest.
 * 
 * Calls to add(...) and setLayout(...) will
 * be forwarded to the content section, not this
 * entire component.
 * 
 * @author Matt Crow
 */
public class Page extends JPanel{
    private final JMenuBar menuBar;
    private final JPanel content;
    private boolean hasInstantiated = false;
    
    public Page(){
        super.setLayout(new BorderLayout());
        
        menuBar = new JMenuBar();
        super.add(menuBar, BorderLayout.PAGE_START);
        
        content = new JPanel();
        super.add(content, BorderLayout.CENTER);
        
        hasInstantiated = true;
        setFocusable(true);
        
        setBackground(CustomColors.black);
        Style.applyStyling(this);
        Style.applyStyling(menuBar);
        Style.applyStyling(content);
    }
    
    /**
     * Used to get the MainWindow this is
     * contained in. Use getHost().switchToPage(...)
     * to switch between pages.
     * 
     * @return the Main Window that is rendering Orpheus
     */
    public MainWindow getHost(){
        return MainWindow.getInstance();
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
        JButton b = new JButton(text);
        b.addActionListener((e)->{
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
     * Adds a component to the menu bar, then resizes the bar to accommodate it.
     * @param c the component to add
     * @return this
     */
    public Page addMenuItem(Component c){
        if(c == null){
            throw new NullPointerException();
        }
        Style.applyStyling(c);
        menuBar.add(c);
        menuBar.setLayout(new GridLayout(1, menuBar.getComponentCount()));
        return this;
    }
    
    /**
     * Sets the layout <b> of the content section, not this entire component. </b>
     * @param l the LayoutManager to apply to the content section
     */
    @Override
    public void setLayout(LayoutManager l){
        if(hasInstantiated){
            content.setLayout(l);
        } else {
            super.setLayout(l);
        }
    }
    
    //this is the supermethod called by all variations of add
    @Override
    public void addImpl(Component comp, Object constraints, int index){
        if(hasInstantiated){
            content.add(comp, constraints, index);
        } else {
            super.addImpl(comp, constraints, index);
        }
        Style.applyStyling(comp);
    }
    
    /**
     * Registers a key control to the Page.
     * For example,
     * <br>
     * {@code
     *  registerKey(KeyEvent.VK_X, true, ()->foo());
     * }
     * <br>
     * will cause foo to run whenever the user presses the 'x' key.
     * 
     * @param key the KeyEvent code for the key to react to
     * @param pressed whether to react when the key is pressed or released
     * @param r the runnable to run when the key is pressed/released
     */
    public void registerKey(int key, boolean pressed, Runnable r){
        String text = key + ((pressed) ? " pressed" : " released");
        getInputMap().put(KeyStroke.getKeyStroke(key, 0, !pressed), text);
        getActionMap().put(text, new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        });
    }
    
    public void close(){
		SwingUtilities.getWindowAncestor(this).dispose();
	}
}
