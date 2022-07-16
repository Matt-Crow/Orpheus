package orpheus.client.gui.components;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Uses the factory design pattern to produce the components needed by the GUI
 * @author Matt Crow
 */
public class ComponentFactory {
    
    public JLabel makeLabel(String text){
        JLabel label = new JLabel(text);
        //Style.applyStyling(label);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
}
