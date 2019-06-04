package windows;

import battle.Battle;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.ArrayList;

import gui.OptionBox;

import battle.Team;
import customizables.*;
import controllers.Master;
import controllers.World;
import graphics.Map;
import graphics.Tile;

@SuppressWarnings("serial")
public class MainCanvas extends DrawingPlane{
	private Team team1;
	private Team team2;
	
	private final OptionBox<String> playerBuild;
	private final OptionBox<String> team1Size;
	private final OptionBox<String> team2Size;
	
	public MainCanvas(){
		super();
		
		JButton b = new JButton("Quit");
        
        MainCanvas m = this;
        
		b.addActionListener(new AbstractAction(){
            @Override
			public void actionPerformed(ActionEvent e){
				m.switchTo(new StartCanvas());
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
		
		JButton newBuild = new JButton("Customize");
		newBuild.addActionListener(new AbstractAction(){
            @Override
			public void actionPerformed(ActionEvent e){
				switchTo(new BuildCanvas());
			}
		});
		add(newBuild);

        DrawingPlane p = this;
        
		JButton battle = new JButton("Battle");
		battle.addActionListener(new AbstractAction(){
            @Override
			public void actionPerformed(ActionEvent e){
				startBattle();
			}
		});
		addMenuItem(battle);
		
		playerBuild = new OptionBox<>("Player build", buildNameList);
		playerBuild.addActionListener(getRepaint());
		add(playerBuild);
		
		team1Size = new OptionBox<>("Team 1 size", numStr);
		team1Size.addActionListener(getRepaint());
		add(team1Size);
		
		team2Size = new OptionBox<>("Team 2 size", numStr);
		team2Size.addActionListener(getRepaint());
		add(team2Size);
		
		resizeComponents(2, 2);
		resizeMenu(2);
	}
    
    //this is a lot of work for one function, might want to clean it up a bit
    private void startBattle(){
        team1 = Team.constructRandomTeam("Team 1", Color.green, Integer.parseInt(team1Size.getSelected()) - 1);
        team2 = Team.constructRandomTeam("Team 2", Color.red, Integer.parseInt(team2Size.getSelected()));

        Master.TRUEPLAYER.applyBuild(Build.getBuildByName(playerBuild.getSelected()));
        team1.addMember(Master.TRUEPLAYER);

        World battleWorld = new World(20);
        //it's like a theme park or something
        battleWorld.createCanvas();
        
        Map m = new Map(20);
        Tile block = new Tile(0, 0, Color.red);
        block.setBlocking(true);
        m
            .addToTileSet(0, new Tile(0, 0, Color.BLUE))
            .addToTileSet(1, block);
        m
            .setTile(8, 10, 1)
            .setTile(8, 11, 1)
            .setTile(8, 12, 1)
            .setTile(7, 12, 1)
            .setTile(7, 13, 1)
            .setTile(7, 14, 1)
            .setTile(8, 14, 1)
            .setTile(9, 14, 1)
            .setTile(10, 14, 1)
            .setTile(10, 13, 1)
            .setTile(10, 10, 1)
            .setTile(10, 11, 1)
            .setTile(10, 12, 1);
        battleWorld.setMap(m);
        
        battleWorld
            .addTeam(team1)
            .addTeam(team2);
        
        Battle b = new Battle(
            battleWorld.getCanvas(),
            team1,
            team2
        );
        battleWorld.setCurrentMinigame(b);
        b.setHost(battleWorld);
        
        battleWorld.init();
        switchTo(battleWorld.getCanvas());
    }
}