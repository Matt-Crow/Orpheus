package gui;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Title extends JLabel{
	public Title(String s){
		super(s);
		Style.applyStyling(this);
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
	}
}
