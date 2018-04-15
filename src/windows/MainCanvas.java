package windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.util.ArrayList;

import gui.Button;

import battle.Team;
import customizables.*;
import initializers.Master;
import resources.DrawingPlane;

// need to add custom build creation
@SuppressWarnings("serial")
public class MainCanvas extends DrawingPlane{
	public static final long serialVersionUID = 1L;
	private Team team1;
	private Team team2;
	
	private JComboBox<String> playerBuild;
	private JComboBox<String> team1Size;
	private JComboBox<String> team2Size;
	
	public MainCanvas(){
		super();
		
		Button b = new Button("Quit");
		b.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new StartWindow();
				close();
			}
		});
		addComp(b);
		
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
		
		//FIXME customcolors
		Button newBuild = new Button("Create a new build");
		newBuild.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new BuildWindow();
				close();
			}
		});
		addComp(newBuild);

		Button battle = new Button("Battle");
		battle.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				team1 = Team.constructRandomTeam("Team 1", Color.green, Integer.parseInt(team1Size.getSelectedItem().toString()) - 1);
				team2 = Team.constructRandomTeam("Team 2", Color.red, Integer.parseInt(team2Size.getSelectedItem().toString()));
				
				Master.TRUEPLAYER.applyBuild(Build.getBuildByName(playerBuild.getSelectedItem().toString()));
				team1.addMember(Master.TRUEPLAYER);
				
				team1.setEnemy(team2);
				team2.setEnemy(team1);
				
				BattleWindow bw = new BattleWindow();
				bw.getCanvas().setBattle(team1, team2);
				close();
			}
		});
		addComp(battle);
		
		addComp(new JLabel("Player team size"));
		addComp(new JLabel("Player Build"));
		addComp(new JLabel("Enemy team size"));
		
		team1Size = new JComboBox<String>(numStr);
		team1Size.addActionListener(getRepaint());
		addComp(team1Size);
		
		playerBuild = new JComboBox<String>(buildNameList);
		playerBuild.addActionListener(getRepaint());
		addComp(playerBuild);
		
		team2Size = new JComboBox<String>(numStr);
		team2Size.addActionListener(getRepaint());
		addComp(team2Size);
		
		
		
		
		resizeComponents(3, 3);
	}
}