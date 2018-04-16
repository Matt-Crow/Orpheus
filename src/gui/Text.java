package gui;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Text extends JTextArea{
	public Text(String s){
		super(s);
		Style.applyStyling(this);
		setEditable(false);
	}
}
