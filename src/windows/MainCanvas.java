package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class MainCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	JPanel p = this;
	public MainCanvas(){
		setLayout(null);
		setBackground(Color.black);
		
		JButton quit = new JButton(new AbstractAction("Quit"){
			@Override
			public void actionPerformed(ActionEvent e){
				new StartWindow();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(p);
				frame.dispose();
			}
		});
		quit.setLayout(null);
		quit.setOpaque(true);
		quit.setBorderPainted(false);
		quit.setBounds(0, 0, 100, 100);
		quit.setBackground(Color.red);
		add(quit);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
