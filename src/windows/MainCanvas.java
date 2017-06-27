package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.AbstractAction;

import customizables.*;

import resources.EasyButton;
import resources.Menu;

import initializers.Run;

@SuppressWarnings("serial")
public class MainCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	JPanel panel = this;
	Menu classes;
	Menu active1;
	Menu active2;
	Menu active3;
	Menu passives;
	CharacterClass chosenClass;
	
	public MainCanvas(){
		setLayout(null);
		setBackground(Color.black);
		
		EasyButton b = new EasyButton("Quit", 0, 0, 100, 100, Color.red);
		b.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new StartWindow();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
				frame.dispose();
			}
		});
		b.addTo(this);
		
		String[] c = {"Earth", "Fire", "Water", "Air"};
		String[] a = {"?", "??", "???"};
		String[] p = {"?", "??", "???"};
		
		classes = new Menu(c, 100, 0, 100, 100);
		classes.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		
		active1 = new Menu(a, 0, 100, 100, 100);
		active1.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		
		passives = new Menu(p, 300, 0, 100, 100);
		passives.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		classes.addTo(this);
		active1.addTo(this);
		passives.addTo(this);
		
		EasyButton battle = new EasyButton("Battle", 900, 0, 100, 100, Color.red);
		battle.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				Run.player.setClass(chosenClass.name);
				new BattleWindow(600, 600);
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
				frame.dispose();
			}
		});
		battle.addTo(this);
	}
	public CharacterClass findClass(String name){
		switch(name){
			case "Earth":
				return new Earth();
			case "Fire":
				return new Fire();
			case "Water":
				return new Water();
			case "Air":
				return new Air();
		}
		return new Earth();
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		chosenClass = findClass(classes.getSelectedItem().toString());
		chosenClass.displayPopup(100, 100, g);
	}
}
