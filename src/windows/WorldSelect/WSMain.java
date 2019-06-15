package windows.WorldSelect;

import gui.Style;
import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Matt
 */
public class WSMain extends JPanel{
    public WSMain(){
        super();
        setLayout(new GridLayout(1, 3));
        add(soloButton());
        JButton newMulti = new JButton("Host a multiplayer game");
        Style.applyStyling(newMulti);
        add(newMulti);
        JButton joinMulti = new JButton("Join an existing multiplayer game");
        Style.applyStyling(joinMulti);
        add(joinMulti);
        Style.applyStyling(this);
    }
    
    private JButton soloButton(){
        JButton solo = new JButton("Play a game offline");
        Style.applyStyling(solo);
        solo.addActionListener((e)->{
            //((CardLayout)getParent().getParent().getLayout());
        });
        return solo;
    }
}
