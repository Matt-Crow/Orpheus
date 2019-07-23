
package windows;

import controllers.MainWindow;
import gui.Style;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

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
    }
    
    public MainWindow getHost(){
        return MainWindow.getInstance();
    }
    
    public NewPage addBackButton(String text, NewPage p, Runnable onGoBack){
        JButton b = new JButton(text);
        b.addActionListener((e)->{
            getHost().switchToPage(p);
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
}
