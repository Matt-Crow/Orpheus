package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.AbstractAction;

import java.util.ArrayList;

import resources.Op;

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
		
		ArrayList<JButton> buttons = new ArrayList<>();
		
		JButton about = new JButton(new AbstractAction("About this game"){
			@Override
			public void actionPerformed(ActionEvent e){
				Op.add("about");
				Op.dp();
			}
		});
		buttons.add(about);
		
		JButton play = new JButton(new AbstractAction("Play"){
			@Override
			public void actionPerformed(ActionEvent e){
				new MainWindow();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(p);
				frame.dispose();
			}
		});
		buttons.add(play);
		
		JButton how = new JButton(new AbstractAction("How to play"){
			@Override
			public void actionPerformed(ActionEvent e){
				Op.add("how");
				Op.dp();
			}
		});
		buttons.add(how);
		
		ArrayList<Color> colors = new ArrayList<>();
		colors.add(Color.blue);
		colors.add(Color.red);
		colors.add(Color.green);
		
		int x = 0;
		
		for(JButton button : buttons){
			button.setLayout(null);
			button.setOpaque(true);
			button.setBorderPainted(false);
			button.setBounds(x * 200, 200, 200, 200);
			button.setBackground(colors.get(x));
			add(button);
			x += 1;
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}
