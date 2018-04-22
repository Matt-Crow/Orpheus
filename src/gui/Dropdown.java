package gui;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class Dropdown<T> extends JComboBox<T>{
	public Dropdown(T[] options){
		super(options);
		Style.applyStyling(this);
	}
}
