package orpheus.client.gui.components;

import java.awt.Component;
import javax.swing.*;
import javax.swing.border.*;

public class Style {

    private final static Border b = BorderFactory.createCompoundBorder(
            BorderFactory.createBevelBorder(BevelBorder.RAISED),
            BorderFactory.createBevelBorder(BevelBorder.RAISED)
    );

    public static void applyStyling(Component c) {
        /*
        if (c instanceof JComponent) {
            JComponent j = (JComponent) c;
            j.setOpaque(true);
            j.setBorder(b);
        }
        //don't do else if for this first one
        
        if (c instanceof JButton) {
            JButton bu = (JButton) c;
            bu.setVerticalTextPosition(SwingConstants.CENTER);
            bu.setHorizontalTextPosition(SwingConstants.CENTER);
        }
        c.setVisible(true);

        if (!(c instanceof JTextComponent)) {
            c.setBackground(CustomColors.gold);
        }
        */
    }
}
