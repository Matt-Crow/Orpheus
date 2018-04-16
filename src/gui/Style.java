package gui;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import graphics.CustomColors;

public class Style {
	public static void applyStyling(JComponent j){
		j.setOpaque(true);
		j.setVisible(true);
		j.setBackground(CustomColors.gold);
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		j.setBorder(BorderFactory.createCompoundBorder(b, b));
	}
}
