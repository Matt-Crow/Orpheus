package windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.util.ArrayList;
import initializers.Master;
import customizables.Build;
import entities.Player;
import resources.DrawingPlane;
import resources.EasyButton;

@SuppressWarnings("unused")
public class BuildCanvas extends DrawingPlane{
	public static final long serialVersionUID = 1L;
	private JComboBox<String> baseBuild;
	private JComboBox<String> chooseAct1;
	private JComboBox<String> chooseAct2;
	private JComboBox<String> chooseAct3;
	private JComboBox<String> choosePas1;
	private JComboBox<String> choosePas2;
	private JComboBox<String> choosePas3;
	
	private EasyButton setClass;
	private EasyButton finish;
	private JTextField name;
	private Player testPlayer;
	
	public BuildCanvas(int windowWidth, int windowHeight){
		super(windowWidth, windowHeight);
		
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
				new Build(name.getText(), b.getClassName(), 
						chooseAct1.getSelectedItem().toString(), chooseAct2.getSelectedItem().toString(), chooseAct3.getSelectedItem().toString(), 
						choosePas1.getSelectedItem().toString(), choosePas2.getSelectedItem().toString(), choosePas3.getSelectedItem().toString());
				new MainWindow();
				close();
			}
		});
		addComp(finish);
		
		//tooltip of selected item desc 
		
		// can't make array of jcomboboxes :(
		chooseAct1 = new JComboBox<String>(testPlayer.getCharacterClass().getAttacksString());
		chooseAct1.addActionListener(getRepaint());
		chooseAct1.setSelectedItem(b.getActiveNames()[0]);
		addComp(chooseAct1);
		
		addComp(new VoidComponent());
		
		choosePas1 = new JComboBox<String>(testPlayer.getCharacterClass().getPassivesString());
		choosePas1.addActionListener(getRepaint());
		choosePas1.setSelectedItem(b.getPassiveNames()[0]);
		addComp(choosePas1);
		
		
		chooseAct2 = new JComboBox<String>(testPlayer.getCharacterClass().getAttacksString());
		chooseAct2.addActionListener(getRepaint());
		chooseAct2.setSelectedItem(b.getActiveNames()[1]);
		addComp(chooseAct2);
		
		addComp(new VoidComponent());

		choosePas2 = new JComboBox<String>(testPlayer.getCharacterClass().getPassivesString());
		choosePas2.addActionListener(getRepaint());
		choosePas2.setSelectedItem(b.getPassiveNames()[1]);
		addComp(choosePas2);
		
		
		chooseAct3 = new JComboBox<String>(testPlayer.getCharacterClass().getAttacksString());
		chooseAct3.addActionListener(getRepaint());
		chooseAct3.setSelectedItem(b.getActiveNames()[2]);
		addComp(chooseAct3);
		
		addComp(new VoidComponent());
		
		choosePas3 = new JComboBox<String>(testPlayer.getCharacterClass().getPassivesString());
		choosePas3.addActionListener(getRepaint());
		choosePas3.setSelectedItem(b.getPassiveNames()[2]);
		addComp(choosePas3);
		
		resizeComponents(4, 3);
		revalidate();
		repaint();
	}
}
