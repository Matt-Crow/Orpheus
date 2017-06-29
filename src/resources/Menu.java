package resources;

import javax.swing.JPanel;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class Menu extends JComboBox<Object>{
	MenuNameRender renderer;
	
	public Menu(Object[] contents, int x, int y, int w, int h){
		super(contents);
		setOpaque(true);
		setBounds(x, y, w, h);
		renderer = new MenuNameRender();
		renderer.setSize(100, 100);
		setRenderer(renderer);
	}
	public void addTo(JPanel j){
		j.add(this);
	}
}
