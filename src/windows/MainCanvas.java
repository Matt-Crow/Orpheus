package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.AbstractAction;

import customizables.*;
import attacks.*;
import resources.EasyButton;
import resources.Menu;
import resources.Op;
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
	AbstractAction rep;
	AbstractAction classChosen;
	CharacterClass chosenClass;
	String[] chosenActiveNames;
	
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
		
		chosenClass = new Earth();
		String[] c = {"Earth", "Fire", "Water", "Air"};
		String[] a = chosenClass.getAttackNames();
		String[] p = {"?", "??", "???"};
		
		rep = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		};
		classChosen = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				updateFields();
			}
		};
		
		classes = new Menu(c, 100, 0, 100, 100);
		classes.addActionListener(classChosen);
		classes.addActionListener(rep);
		active1 = new Menu(a, 100, 100, 100, 100);
		active1.addActionListener(rep);
		active2 = new Menu(a, 200, 100, 100, 100);
		active2.addActionListener(rep);
		active3 = new Menu(a, 300, 100, 100, 100);
		active3.addActionListener(rep);
		passives = new Menu(p, 0, 200, 100, 100);
		passives.addActionListener(rep);
		
		classes.addTo(this);
		active1.addTo(this);
		active2.addTo(this);
		active3.addTo(this);
		passives.addTo(this);
		
		EasyButton battle = new EasyButton("Battle", 900, 0, 100, 100, Color.red);
		// works
		battle.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				Run.player.setClass(chosenClass.getName());
				Run.player.getCharacterClass().setActive(chosenActiveNames[0], 0);
				Run.player.getCharacterClass().setActive(chosenActiveNames[1], 1);
				Run.player.getCharacterClass().setActive(chosenActiveNames[2], 2);
				new BattleWindow(600, 600);
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
				frame.dispose();
			}
		});
		battle.addTo(this);
		
		chosenActiveNames = new String[3];
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
	public void updateFields(){
		//a = chosenClass.getAttackNames();
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		chosenClass = findClass(classes.getSelectedItem().toString());
		chosenClass.displayPopup(100, 100, g);
		chosenActiveNames[0] = active1.getSelectedItem().toString();
		chosenActiveNames[1] = active2.getSelectedItem().toString();
		chosenActiveNames[2] = active3.getSelectedItem().toString();
	}
}
