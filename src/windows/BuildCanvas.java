package windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.util.ArrayList;
import initializers.Master;
import customizables.Build;
import entities.Player;
import resources.DrawingPlane;
import resources.EasyButton;
import graphics.UpgradableSelector;
import upgradables.AbstractUpgradable;
import passives.Passive;

//@SuppressWarnings("unused")
public class BuildCanvas extends DrawingPlane{
	public static final long serialVersionUID = 1L;
	private JComboBox<String> baseBuild;
	private UpgradableSelector[] upgradableSelectors;
	
	private EasyButton setClass;
	private EasyButton finish;
	private JTextField name;
	private Player testPlayer;
	
	// currently broken, need to work on Build
	
	public BuildCanvas(){
		super();
		
		//customcolors
		EasyButton quit = new EasyButton("Quit without saving", Color.red);
		quit.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				new MainWindow();
				close();
			}
		});
		addComp(quit);
		
		
		ArrayList<Build> builds = Build.getAllBuilds();
		String[] buildNames = new String[builds.size()];
		for(int i = 0; i < builds.size(); i++){
			buildNames[i] = builds.get(i).getName();
		}
		
		baseBuild = new JComboBox<String>(buildNames);
		baseBuild.addActionListener(getRepaint());
		addComp(baseBuild);
		
		setClass = new EasyButton("Edit/copy build", Color.white);
		setClass.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				removeComp(baseBuild);
				removeComp(setClass);
				phase2();
			}
		});
		addComp(setClass);
		resizeComponents(4, 3);
	}
	public void phase2(){
		Player testPlayer = new Player("TEST");
		Build b = Build.getBuildByName(baseBuild.getSelectedItem().toString());
		testPlayer.applyBuild(b);

		name = new JTextField(b.getName());
		name.setLayout(null);
		addComp(name);
		
		finish = new EasyButton("Save", Color.yellow);
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
		addComp(finish);
		
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
			addComp(upgradableSelectors[i]);
		}
		
		
		
		//add tooltip of selected item desc instead of UpgradableSelector
		
		resizeComponents(3, 3);
		revalidate();
		repaint();
	}
}
