package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.AbstractAction;

import resources.Op;
import resources.EasyButton;

@SuppressWarnings("serial")
public class StartCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	
	public StartCanvas(){
		setLayout(null);
		JPanel p = this;
		
		JLabel title = new JLabel("The Orpheus Proposition");
		title.setLayout(null);
		title.setBounds(0, 0, 600, 200);
		title.setBackground(Color.yellow);
		title.setOpaque(true);
		add(title);
		
		EasyButton about = new EasyButton("About this game", 0, 200, 200, 200, Color.blue);
		about.addActionListener(new AbstractAction("About this game"){
			public void actionPerformed(ActionEvent e){
				Op.add("about");
				Op.dp();
			}
		});
		about.addTo(this);
		
		EasyButton play = new EasyButton("Play", 200, 200, 200, 200, Color.red);
				
		play.addActionListener(new AbstractAction("Play"){
			public void actionPerformed(ActionEvent e){
				new MainWindow();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(p);
				frame.dispose();
			}
		});
		play.addTo(this);
		
		EasyButton how = new EasyButton("How to play", 400, 200, 200, 200, Color.green);
		how.addActionListener(new AbstractAction("How to play"){
			public void actionPerformed(ActionEvent e){
				Op.add("how");
				Op.dp();
			}
		});
		how.addTo(this);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
