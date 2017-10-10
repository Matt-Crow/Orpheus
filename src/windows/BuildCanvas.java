package windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import java.util.ArrayList;
import initializers.Master;
import customizables.Build;
import entities.Player;
import resources.DrawingPlane;
import resources.Menu;
import resources.EasyButton;

public class BuildCanvas extends DrawingPlane{
	public static final long serialVersionUID = 1L;
	private Menu baseBuild;
	private Menu chooseAct1;
	private Menu chooseAct2;
	private Menu chooseAct3;
	private Menu choosePas1;
	private Menu choosePas2;
	private Menu choosePas3;
	
	private EasyButton setClass;
	private EasyButton finish;
	
	private Player testPlayer;
	
	public BuildCanvas(int windowWidth, int windowHeight){
		super(windowWidth, windowHeight);
		//customcolors
		EasyButton quit = new EasyButton("Quit without saving", 0, 0, Master.CANVASWIDTH / 10, Master.CANVASHEIGHT / 10, Color.red);
		quit.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				new MainWindow();
				close();
			}
		});
		quit.addTo(this);
		
		
		ArrayList<Build> builds = Build.getAllBuilds();
		String[] buildNames = new String[builds.size()];
		for(int i = 0; i < builds.size(); i++){
			buildNames[i] = builds.get(i).getName();
		}
		
		baseBuild = new Menu(buildNames, Master.CANVASWIDTH / 2, Master.CANVASHEIGHT / 2, 100, 100);
		baseBuild.addActionListener(getRepaint());
		baseBuild.addTo(this);
		
		setClass = new EasyButton("Edit/copy build", Master.CANVASWIDTH / 2, Master.CANVASHEIGHT / 2, 300, 100, Color.white);
		setClass.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				baseBuild.setEnabled(false);
				setClass.setEnabled(false);
				setClass.setVisible(false);
				phase2();
			}
		});
		setClass.addTo(this);
	}
	public void phase2(){
		Player testPlayer = new Player("TEST");
		Build b = Build.getBuildByName(baseBuild.getSelectedItem().toString());
		testPlayer.applyBuild(b);
		
		Menu[] acts = {chooseAct1, chooseAct2, chooseAct3};
		Menu[] pass = {choosePas1, choosePas2, choosePas3};
		
		for(int i = 0; i < 3; i++){
			acts[i] = new Menu(testPlayer.getCharacterClass().getAttacksString(), 0, Master.CANVASHEIGHT / 4 * (i + 1), 100, 100);
			acts[i].addActionListener(getRepaint());
			acts[i].setSelectedItem(b.getActiveNames()[i]);
			acts[i].addTo(this);
			
			pass[i] = new Menu(testPlayer.getCharacterClass().getPassivesString(), Master.CANVASWIDTH - 100, Master.CANVASHEIGHT / 4 * (i + 1), 100, 100);
			pass[i].addActionListener(getRepaint());
			pass[i].setSelectedItem(b.getPassiveNames()[i]);
			pass[i].addTo(this);
		}
		
		finish = new EasyButton("Save", Master.CANVASWIDTH - 100, 0, 100, 100, Color.yellow);
		finish.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				//so ugly
				new Build("TEST: SETNAME LATER", b.getClassName(), acts[0].getSelectedItem().toString(), acts[1].getSelectedItem().toString(), acts[2].getSelectedItem().toString(), pass[0].getSelectedItem().toString(), pass[1].getSelectedItem().toString(), pass[2].getSelectedItem().toString());
				new MainWindow();
				close();
			}
		});
		finish.addTo(this);
	}
}
