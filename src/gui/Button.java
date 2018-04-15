package gui;

import javax.swing.JButton;
import javax.swing.border.BevelBorder;

import graphics.CustomColors;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Button extends JButton{
	public Button(String s){
		super(s);
		setBackground(CustomColors.gold);
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED);
		
		setBorder(BorderFactory.createCompoundBorder(b, b));
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
	}
}
