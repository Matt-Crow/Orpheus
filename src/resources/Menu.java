package resources;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class Menu extends JComboBox<Object>{
	
	public Menu(String[] contents, int x, int y, int w, int h){
		super(contents);
		setOpaque(true);
		setBounds(x, y, w, h);
	}
}
