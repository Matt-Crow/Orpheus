package windows;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import actives.AbstractActive;
import actives.ActiveStat;

import java.util.ArrayList;
import customizables.Build;
import customizables.CharacterClass;
import customizables.CharacterStat;
import entities.Player;
import gui.Button;
import gui.OptionBox;
import gui.Pane;
import gui.UpgradableSelector;
import passives.AbstractPassive;
import passives.PassiveStat;
import gui.Text;
import upgradables.AbstractUpgradable;

//need to redo this
@SuppressWarnings("serial")
public class BuildCanvas extends DrawingPlane{
	private OptionBox<String> baseBuild;
	private UpgradableSelector<CharacterStat> classSelect;
	private UpgradableSelector<ActiveStat>[] actives;
	private UpgradableSelector<PassiveStat>[] passives;
	
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
		
		classSelect = new UpgradableSelector<CharacterStat>("Character Class", CharacterClass.getAll());
		classSelect.getBox().setSelected(b.getClassName());
		add(classSelect);
		
		actives = new UpgradableSelector[3];
		for(int i = 0; i < 3; i++){
			AbstractActive[] options = AbstractActive.getAll();
			actives[i] = new UpgradableSelector<ActiveStat>("Active #" + (i + 1), options);
			actives[i].getBox().setSelected(b.getActiveNames()[i]);
			add(actives[i]);
		}
		
		passives = new UpgradableSelector[3];
		for(int i = 0; i < 3; i++){
			AbstractPassive[] options = AbstractPassive.getAll();
			passives[i] = new UpgradableSelector<PassiveStat>("Passive #" + (i + 1), options);
			passives[i].getBox().setSelected(b.getPassiveNames()[i]);
			add(passives[i]);
		}
		
		finish = new Button("Save");
		finish.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				new Build(
						name.getText(), 
						classSelect.getBox().getSelected().getName(), 
						actives[0].getBox().getSelected().getName(), 
						actives[1].getBox().getSelected().getName(), 
						actives[2].getBox().getSelected().getName(), 
						passives[0].getBox().getSelected().getName(), 
						passives[1].getBox().getSelected().getName(), 
						passives[2].getBox().getSelected().getName()
								);
				new MainWindow();
				close();
			}
		});
		addMenuItem(finish);
		
		
		//upgradableSelectors[i].getBox().setSelected(names[i % 3]);
			
		
		
		
		//TODO: add tooltip of selected item desc instead of UpgradableSelector
		
		resizeComponents(3, 3);
		resizeMenu(2);
		revalidate();
		repaint();
	}
}
