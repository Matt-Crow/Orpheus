package orpheus.client.gui.pages.start;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.PageController;

/**
 *
 * @author Matt
 */
public class StartTextDisplay extends Page{
    public StartTextDisplay(ClientAppContext context, PageController host, String displayText){
        super(context, host);
        addBackButton(()-> new StartPage(context, host));
        
        var cf = context.getComponentFactory();

        /*
        Layout: text occupies the middle 50% of the screen
        */
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(cf.makePanel(), gbc.clone());
        
        JScrollPane scrolly = new JScrollPane(cf.makeTextArea(displayText));
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        gbc.gridwidth = 2;
        add(scrolly, gbc.clone());
        
        gbc.gridwidth = 1;
        add(cf.makePanel(), gbc.clone());
    }
}
