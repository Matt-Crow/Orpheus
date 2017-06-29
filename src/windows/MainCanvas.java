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
	Attack[] chosenActives;
	
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
		
		CharacterClass[] c = {new Earth(), new Fire(), new Water(), new Air()};
		Attack[] a = {new HeavyStroke(), new Fireball(), new FieldsOfFire()};
		String[] p = {"?", "??", "???"};
		
		classes = new Menu(c, 100, 0, 100, 100);
		classes.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		
		active1 = new Menu(a, 100, 100, 100, 100);
		active1.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		active2 = new Menu(a, 200, 100, 100, 100);
		active2.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		active3 = new Menu(a, 300, 100, 100, 100);
		active3.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		passives = new Menu(p, 0, 200, 100, 100);
		passives.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		});
		classes.addTo(this);
		active1.addTo(this);
		active2.addTo(this);
		active3.addTo(this);
		passives.addTo(this);
		
		EasyButton battle = new EasyButton("Battle", 900, 0, 100, 100, Color.red);
		battle.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				Run.player.setClass(chosenClass.getName());
				Run.player.getCharacterClass().setActive(chosenActives[0], 0);
				Run.player.getCharacterClass().setActive(chosenActives[1], 1);
				Run.player.getCharacterClass().setActive(chosenActives[2], 2);
				new BattleWindow(600, 600);
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
				frame.dispose();
			}
		});
		battle.addTo(this);
		
		chosenActives = new Attack[3];
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
		chosenActives[0] = (Attack) active1.getSelectedItem();
		chosenActives[1] = (Attack) active2.getSelectedItem();
		chosenActives[2] = (Attack) active3.getSelectedItem();
	}
}
