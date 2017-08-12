package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.AbstractAction;
import java.util.ArrayList;

import battle.Team;
import customizables.*;
import resources.EasyButton;
import resources.Menu;
import initializers.Master;
import initializers.Run;

// need to add custom build creation
@SuppressWarnings("serial")
public class MainCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	private JPanel panel = this;
	private AbstractAction rep;
	private Team team1;
	private Team team2;
	
	private Menu playerBuild;
	
	public MainCanvas(){
		setLayout(null);
		setBackground(Color.black);
		
		rep = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		};
		
		EasyButton b = new EasyButton("Quit", 0, 0, Master.CANVASWIDTH / 10, Master.CANVASHEIGHT / 10, Color.red);
		b.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new StartWindow();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
				frame.dispose();
			}
		});
		b.addTo(this);
		
		ArrayList<Build> builds = Build.getAllBuilds();
		String[] buildNameList = new String[builds.size()];
		for(int i = 0; i < builds.size(); i++){
			buildNameList[i] = builds.get(i).getName();
		}
		
		playerBuild = new Menu(buildNameList, Master.CANVASWIDTH / 2, Master.CANVASHEIGHT / 2, 100, 100);
		playerBuild.addActionListener(rep);
		playerBuild.addTo(this);
		
		EasyButton battle = new EasyButton("Battle", 900, 0, 100, 100, Color.red);
		battle.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				
				team1 = Team.constructRandomTeam("Team 1", Color.green, 4);
				team2 = Team.constructRandomTeam("Team 2", Color.red, 5);
				
				Run.player.applyBuild(Build.getBuildByName(playerBuild.getSelectedItem().toString()));
				Run.player.disableAI();
				team1.addMember(Run.player);
				
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