package orpheus.client.gui.pages;

import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.Toolkit;
import javax.swing.JPanel;

import orpheus.client.AppContext;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.start.StartPage;

/**
 * controls which page is currently rendered
 *
 * @author Matt Crow
 */
public class PageController extends JFrame {
    private final JPanel content;
    private Page currentPage;
    
    public PageController(AppContext context, ComponentFactory components) {        
        setTitle("The Orpheus Proposition");

        content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(
            (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
            (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration()).bottom
        );
        setVisible(true);

        currentPage = null;

        switchToPage(new StartPage(context, this, components));
    }

    public PageController switchToPage(Page p) {
        if (p == null) {
            throw new NullPointerException();
        }
        content.removeAll();
        content.add(p.getMenuBar(), BorderLayout.PAGE_START);
        content.add(p, BorderLayout.CENTER);
        content.revalidate();
        p.requestFocus();
        content.repaint();
        
        if(currentPage != null){
            currentPage.leavingPage();
        }
        currentPage = p;
        currentPage.enteredPage();
        
        return this;
    }
}
