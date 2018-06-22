package windows;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import actives.AbstractActive;

import java.util.ArrayList;
import customizables.Build;
import entities.Player;
import gui.Button;
import gui.OptionBox;
import gui.Pane;
import gui.UpgradableSelector;
import passives.AbstractPassive;
import gui.Text;
import upgradables.AbstractUpgradable;

@SuppressWarnings("serial")
public class BuildCanvas extends DrawingPlane{
	private OptionBox<String> baseBuild;
	private UpgradableSelector[] upgradableSelectors;
	
	private Button temp;
	
	private Button setClass;
	private Button finish;
	private Text name;
	private Player testPlayer;
	
	public BuildCanvas(){
		super();
		Button quit = new Button("Quit without saving");
		quit.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				new MainWindow();
				close();
			}
		});
		addMenuItem(quit);
		
		temp = new Button("customize upgradables");
		temp.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new CustomizeWindow();
				close();
			}
		});
		add(temp);
		
		ArrayList<Build> builds = Build.getAllBuilds();
		String[] buildNames = new String[builds.size()];
		for(int i = 0; i < builds.size(); i++){
			buildNames[i] = builds.get(i).getName();
		}
		
		baseBuild = new OptionBox<String>("Base build", buildNames);
		baseBuild.addActionListener(getRepaint());
		add(baseBuild);
		
		setClass = new Button("Edit/copy build");
		setClass.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				remove(baseBuild);
				remove(setClass);
				phase2();
			}
		});
		add(setClass);
		
		resizeComponents(1, 3);
		resizeMenu(1);
	}
	public void phase2(){
		remove(temp);
		testPlayer = new Player("TEST");
		Build b = Build.getBuildByName(baseBuild.getSelected().toString());
		testPlayer.applyBuild(b);
		
		add(new Pane());
		name = new Text(b.getName());
		name.setEditable(true);
		add(name);
		add(new Pane());
		
		finish = new Button("Save");
		finish.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				String[] s = new String[6];
				for(int i = 0; i < 6; i++){
					s[i] = upgradableSelectors[i].getBox().getSelected().toString();
				}
				new Build(name.getText(), b.getClassName(), s[0], s[1], s[2], s[3], s[4], s[5]);
				new MainWindow();
				close();
			}
		});
		addMenuItem(finish);
		
		upgradableSelectors = new UpgradableSelector[6];
		
		for(int i = 0; i < 6; i++){
			AbstractUpgradable[] options;
			
			if(i <= 2){
				String[] names = AbstractActive.getAllNames();
				options = new AbstractUpgradable[names.length];
				for(int ind = 0; ind < names.length; ind++){
					options[ind] = AbstractActive.getActiveByName(names[ind]);
				}
			} else {
				String[] names = AbstractPassive.getAllNames();
				options = new AbstractUpgradable[names.length];
				for(int ind = 0; ind < names.length; ind++){
					options[ind] = AbstractPassive.getPassiveByName(names[ind]);
				}
			}
					 
			String title = (i <= 2) ? "Active" : "Passive";
			String[] names = (i <= 2) ? b.getActiveNames() : b.getPassiveNames();
			
			upgradableSelectors[i] = new UpgradableSelector(title + " #" + (i % 3 + 1), options);
			upgradableSelectors[i].getBox().setSelected(names[i % 3]);
			add(upgradableSelectors[i]);
		}
		
		
		
		//TODO: add tooltip of selected item desc instead of UpgradableSelector
		
		resizeComponents(3, 3);
		resizeMenu(2);
		revalidate();
		repaint();
	}
}
