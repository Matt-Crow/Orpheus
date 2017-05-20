package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import resources.Op;

import javax.swing.JLabel;
import javax.swing.JButton;

public class StartCanvas extends JPanel implements ActionListener{
	public static final long serialVersionUID = 1L;
	
	public StartCanvas(){
		setLayout(null);
		
		JLabel title = new JLabel("The Orpheus Proposition");
		title.setLayout(null);
		title.setBounds(0, 0, 600, 200);
		title.setBackground(Color.yellow);
		title.setOpaque(true);
		add(title);
		
		JButton about = new JButton("About this game");
		about.setLayout(null);
		about.setBounds(0, 200, 200, 200);
		about.setBackground(Color.blue);
		about.setOpaque(true);
		about.addActionListener(this);
		add(about);
		
		JButton play = new JButton("Play");
		play.setLayout(null);
		play.setBounds(200, 200, 200, 200);
		play.setBackground(Color.red);
		play.setOpaque(true);
		play.addActionListener(this);
		add(play);
		
		JButton how = new JButton("How to play");
		how.setLayout(null);
		how.setBounds(400, 200, 200, 200);
		how.setBackground(Color.green);
		how.setOpaque(true);
		how.addActionListener(this);
		add(how);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	@Override
	public void actionPerformed(ActionEvent event){
		Op.add("Click");
		Op.dp();
	}
}
