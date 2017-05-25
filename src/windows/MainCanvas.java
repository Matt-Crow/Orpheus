package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.AbstractAction;

import resources.EasyButton;;

@SuppressWarnings("serial")
public class MainCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	JPanel p = this;
	public MainCanvas(){
		setLayout(null);
		setBackground(Color.black);
		
		EasyButton b = new EasyButton("Quit", 0, 0, 100, 100, Color.red);
		b.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new StartWindow();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(p);
				frame.dispose();
			}
		});
		b.addTo(this);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
