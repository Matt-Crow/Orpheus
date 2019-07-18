package windows.customize;

import java.awt.GridLayout;
import javax.swing.JButton;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class CustomizeMain extends SubPage{
    public CustomizeMain(Page p){
        super(p);
        setLayout(new GridLayout(1, 4));
        
        JButton act = new JButton("Customize Actives");
        add(act);
        
        JButton pas = new JButton("Customize Passives");
        add(pas);
        
        JButton cha = new JButton("Customize Character Classes");
        add(cha);
        
        JButton bui = new JButton("Customize Builds");
        add(bui);
    }
}
