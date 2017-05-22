package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import resources.Op;

import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.AbstractAction;

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
		
		JButton about = new JButton(new AbstractAction("About this game"){
			@Override
			public void actionPerformed(ActionEvent e){
				Op.add("about");
				Op.dp();
			}
		});
		about.setLayout(null);
		about.setBounds(0, 200, 200, 200);
		about.setBackground(Color.blue);
		about.setOpaque(true);
		add(about);
		
		JButton play = new JButton(new AbstractAction("Play"){
			@Override
			public void actionPerformed(ActionEvent e){
				Op.add("hi");
				Op.dp();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(p);
				frame.dispose();
			}
		});
		play.setLayout(null);
		play.setBounds(200, 200, 200, 200);
		play.setBackground(Color.red);
		play.setOpaque(true);
		add(play);
		
		JButton how = new JButton(new AbstractAction("How to play"){
			@Override
			public void actionPerformed(ActionEvent e){
				Op.add("how");
				Op.dp();
			}
		});
		how.setLayout(null);
		how.setBounds(400, 200, 200, 200);
		how.setBackground(Color.green);
		how.setOpaque(true);
		add(how);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
