
package windows;

import controllers.MainWindow;
import gui.Style;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 *
 * @author Matt Crow
 */
public class NewPage extends JPanel{
    private final MainWindow host;
    private final JMenuBar menuBar;
    private final JPanel content;
    
    public NewPage(MainWindow main){
        host = main;
        super.setLayout(new BorderLayout());
        
        menuBar = new JMenuBar();
        super.add(menuBar, BorderLayout.PAGE_START);
        
        content = new JPanel();
        super.add(content, BorderLayout.CENTER);
    }
    
    public MainWindow getHost(){
        return host;
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
    
    //this is the supermethod called by all variations of add
    @Override
    public void addImpl(Component comp, Object constraints, int index){
        super.addImpl(comp, constraints, index);
        Style.applyStyling(comp);
        //how do I make this redirect the adding to this' content?
    }
}
