
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
 *
 * @author Matt Crow
 */
public class NewPage extends JPanel{
    private final JMenuBar menuBar;
    private final JPanel content;
    private boolean hasInstantiated = false;
    
    public NewPage(){
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
    
    public MainWindow getHost(){
        return MainWindow.getInstance();
    }
    
    public NewPage addBackButton(String text, NewPage p, Runnable onGoBack){
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
    public NewPage addBackButton(String text, NewPage p){
        return addBackButton(text, p, ()->{});
    }
    public NewPage addBackButton(NewPage p){
        return addBackButton("Back", p);
    }
    public NewPage addBackButton(Runnable r){
        return addBackButton("Back", null, r);
    }
    public NewPage addBackButton(NewPage p, Runnable r){
        return addBackButton("Back", p, r);
    }
    
    public NewPage addMenuItem(Component c){
        if(c == null){
            throw new NullPointerException();
        }
        Style.applyStyling(c);
        menuBar.add(c);
        menuBar.setLayout(new GridLayout(1, menuBar.getComponentCount()));
        return this;
    }
    
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
