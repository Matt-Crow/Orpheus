package windows;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.util.ArrayList;
import customizables.Build;
import entities.Player;
import resources.DrawingPlane;
import gui.Button;
import graphics.UpgradableSelector;
import upgradables.AbstractUpgradable;

public class BuildCanvas extends DrawingPlane{
	public static final long serialVersionUID = 1L;
	private JComboBox<String> baseBuild;
	private UpgradableSelector[] upgradableSelectors;
	
	private Button setClass;
	private Button finish;
	private JTextField name;
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
		add(quit);
		
		
		ArrayList<Build> builds = Build.getAllBuilds();
		String[] buildNames = new String[builds.size()];
		for(int i = 0; i < builds.size(); i++){
			buildNames[i] = builds.get(i).getName();
		}
		
		baseBuild = new JComboBox<String>(buildNames);
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
		resizeComponents(4, 3);
	}
	public void phase2(){
		testPlayer = new Player("TEST");
		Build b = Build.getBuildByName(baseBuild.getSelectedItem().toString());
		testPlayer.applyBuild(b);

		name = new JTextField(b.getName());
		add(name);
		
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
		add(finish);
		
		upgradableSelectors = new UpgradableSelector[6];
		for(int i = 0; i < 6; i++){
			ArrayList<AbstractUpgradable> option = (i <= 2) ? 
					new ArrayList<AbstractUpgradable>(testPlayer.getCharacterClass().getAttackOption()) 
					: 
					new ArrayList<AbstractUpgradable>(testPlayer.getCharacterClass().getPassiveOptions()); 
			String title = (i <= 2) ? "Active" : "Passive";
			String[] names = (i <= 2) ? b.getActiveNames() : b.getPassiveNames();
					
			upgradableSelectors[i] = new UpgradableSelector(title + " #" + (i % 3 + 1), option);
			upgradableSelectors[i].getBox().setSelected(names[i % 3]);
			add(upgradableSelectors[i]);
		}
		
		
		
		//TODO: add tooltip of selected item desc instead of UpgradableSelector
		
		resizeComponents(3, 3);
		revalidate();
		repaint();
	}
}
