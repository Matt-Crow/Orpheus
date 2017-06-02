package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.AbstractAction;

import customizables.CharacterClass;
import customizables.Earth;

import resources.EasyButton;
import resources.Menu;

@SuppressWarnings("serial")
public class MainCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	JPanel p = this;
	Menu classes;
	Menu actives;
	Menu passives;
	
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
		
		String[] c = {"Earth", "Fire", "Water", "Air"};
		String[] a = {"?", "??", "???"};
		String[] p = {"?", "??", "???"};
		
		classes = new Menu(c, 100, 0, 100, 100);
		EasyButton openMenu1 = new EasyButton("Menu 1", 100, 0, 100, 100, Color.green);
		openMenu1.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
			}
		});
		
		actives = new Menu(a, 200, 0, 100, 100);
		EasyButton openMenu2 = new EasyButton("Menu 2", 200, 0, 100, 100, Color.green);
		openMenu2.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
			}
		});
		
		passives = new Menu(p, 300, 0, 100, 100);
		EasyButton openMenu3 = new EasyButton("Menu 3", 300, 0, 100, 100, Color.green);
		openMenu3.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
			}
		});
		classes.addTo(this);
		actives.addTo(this);
		passives.addTo(this);
	}
	public CharacterClass findClass(String name){
		switch(name){
			case "Earth":
				return new Earth();
		}
		return new Earth();
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		CharacterClass chosenClass = findClass(classes.getSelectedItem().toString());
		chosenClass.displayPopup(100, 100, g);
	}
}
