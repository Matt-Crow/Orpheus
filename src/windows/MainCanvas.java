package windows;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class MainCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	
	public MainCanvas(){
		setLayout(null);
		setBackground(Color.black);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
