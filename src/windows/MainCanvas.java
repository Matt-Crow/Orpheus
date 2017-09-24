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

// need to add custom build creation
@SuppressWarnings("serial")
public class MainCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	private JPanel panel = this;
	private AbstractAction rep;
	private Team team1;
	private Team team2;
	
	private Menu playerBuild;
	private Menu team1Size;
	private Menu team2Size;
	
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
		
		int[] numbers = new int[]{1, 2, 3, 4, 5, 10};
		String[] numStr = new String[numbers.length];
		
		for(int i = 0; i < numbers.length; i++){
			numStr[i] = Integer.toString(numbers[i]);
		}
		
		team1Size = new Menu(numStr, Master.CANVASWIDTH / 4, Master.CANVASHEIGHT / 2, 100, 100);
		playerBuild = new Menu(buildNameList, Master.CANVASWIDTH / 2, Master.CANVASHEIGHT / 2, 100, 100);
		team2Size = new Menu(numStr, (Master.CANVASWIDTH / 4) * 3, Master.CANVASHEIGHT / 2, 100, 100);
		
		Menu[] menus = new Menu[]{team1Size, playerBuild, team2Size};
		
		for(int i = 0; i < 3; i++){
			menus[i].addActionListener(rep);
			menus[i].addTo(this);
		}
		
		EasyButton battle = new EasyButton("Battle", Master.CANVASWIDTH - 100, 0, 100, 100, Color.red);
		battle.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				team1 = Team.constructRandomTeam("Team 1", Color.green, Integer.parseInt(team1Size.getSelectedItem().toString()) - 1);
				team2 = Team.constructRandomTeam("Team 2", Color.red, Integer.parseInt(team2Size.getSelectedItem().toString()));
				
				Master.thePlayer.applyBuild(Build.getBuildByName(playerBuild.getSelectedItem().toString()));
				team1.addMember(Master.thePlayer);
				
				team1.setEnemy(team2);
				team2.setEnemy(team1);
				
				BattleWindow bw = new BattleWindow();
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