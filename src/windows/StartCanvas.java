package windows;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class StartCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	
	public StartCanvas(){
		JLabel title = new JLabel("Orpheus");
		add(title);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		setBackground(Color.green);
	}
}
