package windows.WorldSelect;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class WSJoin extends SubPage{
    public WSJoin(Page p) {
        super(p);
        //work in progress
        JTextField ip = new JTextField("enter host address here");
        ip.addActionListener((e)->{
            
        });
        add(ip);
    }
}
