package windows.start;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class StartAbout extends SubPage{
    public StartAbout(Page p){
        super(p);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(new JPanel(), gbc.clone());
        
        JTextArea text = new JTextArea("");
        text.setEditable(false);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        InputStream in = StartAbout.class.getResourceAsStream("/README.txt");
        if(in != null){
            BufferedReader buff = new BufferedReader(new InputStreamReader(in));
            try {
                while(buff.ready()){
                    text.append(buff.readLine() + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
        JScrollPane scrolly = new JScrollPane(text);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        gbc.gridwidth = 2;
        add(scrolly, gbc.clone());
        
        gbc.gridwidth = 1;
        add(new JPanel(), gbc.clone());
    }
}
