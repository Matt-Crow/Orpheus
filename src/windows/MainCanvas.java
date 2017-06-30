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
import resources.EasyButton;
import resources.Menu;
import initializers.Run;

// need to add custom build creation
@SuppressWarnings("serial")
public class MainCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	private JPanel panel = this;
	private AbstractAction rep;
	private Team team1;
	private Team team2;
	
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
		
		String[] buildNameList = new String[]{"Default Earth", "Default Fire", "Default Water", "Default Air"}; 
		rep = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		};
		teamBuilds = new Menu[2][5];
		for(int t = 0; t < 2; t++){
			for(int m = 0; m < 5; m++){
				teamBuilds[t][m] = new Menu(buildNameList, 100 + t * 200, 100 + m * 100, 100, 100);
				teamBuilds[t][m].addActionListener(rep);
				teamBuilds[t][m].addTo(this);
			}
		}
		EasyButton battle = new EasyButton("Battle", 900, 0, 100, 100, Color.red);
		battle.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				
				team1 = new Team("Team 1", Color.green);
				team2 = new Team("Team 2", Color.red);
				
				team1.addMember(Run.player);
				String[] t1names = new String[]{"Nick", "Paul", "Gianna", "David"};
				for(String name : t1names){
					team1.addMember(new Player(name));
				}
				for(int i = 1; i <= 5; i++){
					team2.addMember(new Player("Grunt #" + i));
				}
				
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
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
}