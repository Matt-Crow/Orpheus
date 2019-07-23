package windows;

import controllers.MainWindow;
import controllers.Master;
import graphics.CustomColors;
import gui.Style;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import javax.swing.*;
import util.SafeList;

/**
 * The Page class currently works with the SubPage class,
 * but I'm working to change this to just one class per page.
 * 
 * A Page consists of two parts: a menu bar along the top,
 * which shouldn't change once stuff is added to it,
 * and a content section which renders SubPages using
 * a CardLayout.
 * 
 * @author Matt Crow
 */
public class Page extends JPanel{
    private final SafeList<PageSwitchListener> pageSwitchListeners;
    
    private final MainWindow host;
    private final JMenuBar menuBar;
    private final JPanel content;
    private final HashMap<String, SubPage> subPages; //since CardLayout won't let me extract this
    private SubPage currentSubPage;
    
    public Page(MainWindow hostingWindow){
        host = hostingWindow;
        menuBar = new JMenuBar();
        content = new JPanel();
        content.setLayout(new CardLayout());
        
        setLayout(new BorderLayout());
        super.add(menuBar, BorderLayout.PAGE_START);
        super.add(content, BorderLayout.CENTER);
        
        setBackground(CustomColors.black);
        setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
        setFocusable(true);
        
        subPages = new HashMap<>();
        currentSubPage = null;
        
        pageSwitchListeners = new SafeList<>();
        
        Style.applyStyling(this);
        Style.applyStyling(menuBar);
        Style.applyStyling(content);
    }
    
    public JPanel getContent(){
        return content;
    }
    
    /**
     * Adds a component to the menu bar,
     * applies the Orpheus style to it,
     * then resizes the menu bar to accommodate it.
     * 
     * @param c the component to add to the menu.
     * @return this, for chaining purposes.
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
    
    //this is the supermethod called by all variations of add
    @Override
    public void addImpl(Component comp, Object constraints, int index){
        super.addImpl(comp, constraints, index);
        Style.applyStyling(comp);
    }
    
    /**
     * Adds a subpage to this page,
     * allowing the program to easily switch between pages.
     * @param subPageName the string which, when passed to this' 
     * layout's show method, will switch to the given subpage.
     * @param sub the subpage to add to this Page.
     * @return this, for chaining purposes.
     */
    public Page addSubPage(String subPageName, SubPage sub){
        content.add(sub, subPageName);
        subPages.put(subPageName, sub);
        return this;
    }
    
    /**
     * Adds a button to this' menu bar which,
     * when clicked,
     * switches to the given Page.
     * @param goBackTo the Page to go to upon clicking the back button.
     * @return this, for chaining purposes.
     */
    public Page addBackButton(Page goBackTo){
        JButton back = new JButton("Back");
        back.addActionListener((e)->{
            getHost().switchToPage(goBackTo);
        });
        addMenuItem(back);
        return this;
    }
    
    public MainWindow getHost(){
        return host;
    }
    
    /**
     * Switches this' content section to showing
     * the SubPage with the given name.
     * If no page exists with this name,
     * then nothing happens.
     * @param name the name of the SubPage to switch to
     */
    public void switchToSubpage(String name){
        if(!subPages.containsKey(name)){
            throw new IllegalArgumentException("Invalid subpage name: " + name);
        }
        if(currentSubPage != null){
            currentSubPage.switchedFromThis();
        }
        currentSubPage = subPages.get(name);
        if(currentSubPage != null){
            currentSubPage.switchedToThis();
        }
        ((CardLayout)content.getLayout()).show(content, name);
    }
    
    public SubPage getCurrentSubPage(){
        return currentSubPage;
    }
    
    public void addPageSwitchListener(PageSwitchListener l){
        if(l == null){
            throw new NullPointerException();
        }
        pageSwitchListeners.add(l);
    }
    
    public final void switchedToThis(){
        pageSwitchListeners.forEach((l)->{
            l.switchedToPage(this);
        });
    }
    
    public final void switchedFromThis(){
        pageSwitchListeners.forEach((l)->{
            l.leavingPage(this);
        });
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
