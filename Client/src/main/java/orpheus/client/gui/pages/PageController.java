package orpheus.client.gui.pages;

import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.JPanel;
import orpheus.client.gui.components.ComponentFactory;
import orpheus.client.gui.pages.menu.Index;

/**
 * controls which page is currently rendered
 *
 * @author Matt Crow
 */
public class PageController extends JFrame {
    private final JPanel content;
    private Page currentPage;

    
    public PageController(ComponentFactory components) {        
        setTitle("The Orpheus Proposition");

        content = new JPanel();
        content.setLayout(new GridLayout(1, 1));
        setContentPane(content);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(
            (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
            (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration()).bottom
        );
        setVisible(true);

        currentPage = null;

        switchToPage(new Index(this, components));
    }

    public PageController switchToPage(Page p) {
        if (p == null) {
            throw new NullPointerException();
        }
        content.removeAll();
        content.add(p);
        content.revalidate();
        p.requestFocus();
        content.repaint();
        if (currentPage != null && currentPage instanceof PageSwitchListener) {
            ((PageSwitchListener) currentPage).leavingPage(currentPage);
        }
        currentPage = p;
        if (p instanceof PageSwitchListener) {
            ((PageSwitchListener) currentPage).switchedToPage(p);
        }
        return this;
    }
}
