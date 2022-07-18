package orpheus.client.gui.components;

import java.awt.Component;
import javax.swing.*;
import javax.swing.border.*;

import gui.graphics.CustomColors;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.text.JTextComponent;

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
            bu.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    c.setBackground(CustomColors.orange);
                    c.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    c.setBackground(CustomColors.gold);
                    c.repaint();
                }
            });
        } else if (c instanceof JTextArea) {
            JTextArea t = (JTextArea) c;
            t.setEditable(false);
            t.setLineWrap(true);
            t.setWrapStyleWord(true);
        }
        c.setVisible(true);

        if (!(c instanceof JTextComponent)) {
            c.setBackground(CustomColors.gold);
        }
        */
    }
}
