package windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import java.util.ArrayList;

import gui.Button;
import gui.OptionBox;

import battle.Team;
import customizables.*;
import initializers.Master;

@SuppressWarnings("serial")
public class MainCanvas extends DrawingPlane{
	public static final long serialVersionUID = 1L;
	private Team team1;
	private Team team2;
	
	private OptionBox<String> playerBuild;
	private OptionBox<String> team1Size;
	private OptionBox<String> team2Size;
	
	public MainCanvas(){
		super();
		
		Button b = new Button("Quit");
		b.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new StartWindow();
				close();
			}
		});
		addMenuItem(b);
		
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
		
		Button newBuild = new Button("Customize");
		newBuild.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				switchTo(new BuildCanvas());
			}
		});
		add(newBuild);

		Button battle = new Button("Battle");
		battle.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				team1 = Team.constructRandomTeam("Team 1", Color.green, Integer.parseInt(team1Size.getSelected().toString()) - 1);
				team2 = Team.constructRandomTeam("Team 2", Color.red, Integer.parseInt(team2Size.getSelected().toString()));
				
				Master.TRUEPLAYER.applyBuild(Build.getBuildByName(playerBuild.getSelected().toString()));
				team1.addMember(Master.TRUEPLAYER);
				
				team1.setEnemy(team2);
				team2.setEnemy(team1);
				
				BattleWindow bw = new BattleWindow();
				bw.getCanvas().setBattle(team1, team2);
				close();
			}
		});
		addMenuItem(battle);
		
		playerBuild = new OptionBox<String>("Player build", buildNameList);
		playerBuild.addActionListener(getRepaint());
		add(playerBuild);
		
		team1Size = new OptionBox<String>("Team 1 size", numStr);
		team1Size.addActionListener(getRepaint());
		add(team1Size);
		
		team2Size = new OptionBox<String>("Team 2 size", numStr);
		team2Size.addActionListener(getRepaint());
		add(team2Size);
		
		resizeComponents(2, 2);
		resizeMenu(2);
	}
}