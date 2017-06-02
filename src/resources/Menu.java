package resources;

import javax.swing.JPanel;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class Menu extends JComboBox<String>{
	
	public Menu(String[] contents, int x, int y, int w, int h){
		super(contents);
		setOpaque(true);
		setBounds(x, y, w, h);
	}
	public void addTo(JPanel j){
		j.add(this);
	}
}
