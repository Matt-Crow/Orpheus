package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.AbstractAction;

import battle.Team;
import customizables.*;
import entities.Player;
import attacks.*;
import resources.EasyButton;
import resources.Menu;
import resources.Op;
import initializers.Run;

// need to add custom build creation
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
	CharacterClass chosenClass;
	String[] chosenActiveNames;
	Build[] buildList;
	Team team1;
	Team team2;
	
	private Menu[][] teamBuilds;
	
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
		
		
		
		String[] buildNameList = new String[]{"Default Earth", "Default Fire", "Default Water", "Default Air"}; 
		
		
		
		
		rep = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		};
		/*
		classes = new Menu(c, 100, 0, 100, 100);
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
		
		*/
		
		
		teamBuilds = new Menu[2][5];
		for(int t = 0; t < 2; t++){
			for(int m = 0; m < 5; m++){
				teamBuilds[t][m] = new Menu(buildNameList, 100 + t * 200, 100 + m * 100, 100, 100);
				teamBuilds[t][m].addActionListener(rep);
				teamBuilds[t][m].addTo(this);
			}
		}
		
		
		
		EasyButton battle = new EasyButton("Battle", 900, 0, 100, 100, Color.red);
		// works
		battle.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				/*
				Run.player.setClass(chosenClass.getName());
				Run.player.getCharacterClass().setActive(chosenActiveNames[0], 0);
				Run.player.getCharacterClass().setActive(chosenActiveNames[1], 1);
				Run.player.getCharacterClass().setActive(chosenActiveNames[2], 2);
				*/
				team1 = new Team("Team 1", Color.green);
				team2 = new Team("Team 2", Color.red);
				
				team1.addMember(Run.player);
				team1.addMember(new Player("Nick"));
				team1.addMember(new Player("Paul"));
				team1.addMember(new Player("Gianna"));
				team1.addMember(new Player("David"));
				
				team2.addMember(new Player("Grunt #1"));
				team2.addMember(new Player("Grunt #2"));
				team2.addMember(new Player("Grunt #3"));
				team2.addMember(new Player("Grunt #4"));
				team2.addMember(new Player("Grunt #5"));
				
				Build[] t1b = new Build[5];
				Build[] t2b = new Build[5];
				for(int i = 0; i < 5; i++){
					t1b[i] = Build.getBuildByName(teamBuilds[0][i].getSelectedItem().toString());
					t2b[i] = Build.getBuildByName(teamBuilds[1][i].getSelectedItem().toString());
				}
				team1.applyBuilds(t1b);
				team2.applyBuilds(t2b);
				
				team1.setEnemy(team2);
				team2.setEnemy(team1);
				
				BattleWindow bw = new BattleWindow(600, 600);
				bw.getCanvas().setBattle(team1, team2);
				
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
				frame.dispose();
			}
		});
		battle.addTo(this);
		
		//chosenActiveNames = new String[3];
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
		/*
		chosenClass = findClass(classes.getSelectedItem().toString());
		chosenClass.displayPopup(100, 100, g);
		chosenActiveNames[0] = active1.getSelectedItem().toString();
		chosenActiveNames[1] = active2.getSelectedItem().toString();
		chosenActiveNames[2] = active3.getSelectedItem().toString();
		*/
	}
}
