package windows;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

import menus.Menu;

public class MainCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	
	public MainCanvas(){
		setLayout(null);
		setBackground(Color.black);
		Menu m = new Menu("Hi", 0, 0, 100, 100);
		m.set(this);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
