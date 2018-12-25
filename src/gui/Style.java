package gui;

import java.awt.Component;
import javax.swing.*;
import javax.swing.border.*;

import graphics.CustomColors;

public class Style {
    private final static Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    
	public static void applyStyling(Component c){
        if(c instanceof JComponent){
            JComponent j = (JComponent)c;
            j.setOpaque(true);
            j.setBorder(BorderFactory.createCompoundBorder(b, b));
        }
        if(c instanceof JLabel){
            JLabel l = (JLabel)c;
            l.setVerticalAlignment(SwingConstants.CENTER);
            l.setHorizontalAlignment(SwingConstants.CENTER);
	
        }
		c.setVisible(true);
		c.setBackground(CustomColors.gold);
	}
}
