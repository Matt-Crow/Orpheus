package windows.start;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class StartTextDisplay extends SubPage{
    public StartTextDisplay(Page p, String displayText){
        super(p);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(new JPanel(), gbc.clone());
        
        JTextArea text = new JTextArea(displayText);
        text.setEditable(false);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        
        JScrollPane scrolly = new JScrollPane(text);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        gbc.gridwidth = 2;
        add(scrolly, gbc.clone());
        
        gbc.gridwidth = 1;
        add(new JPanel(), gbc.clone());
    }
}
