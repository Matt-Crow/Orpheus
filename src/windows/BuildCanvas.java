package windows;

import customizables.passives.AbstractPassive;
import customizables.passives.PassiveStatName;
import customizables.actives.ActiveStatName;
import customizables.actives.AbstractActive;
import customizables.Build;
import customizables.characterClass.CharacterStatName;
import customizables.characterClass.CharacterClass;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.ArrayList;
import entities.Player;
import gui.*;
import java.io.File;

//need to redo this
@SuppressWarnings("serial")
public class BuildCanvas extends OldContentPage{
	private OptionBox<String> baseBuild;
	private CustomizableSelector<CharacterStatName> classSelect;
	private CustomizableSelector<ActiveStatName>[] actives;
	private CustomizableSelector<PassiveStatName>[] passives;
	
	private JButton temp;
	
	private JButton setClass;
	private JButton finish;
	private JTextArea name;
	private Player testPlayer;
	
	public BuildCanvas(){
		super();
		JButton quit = new JButton("Quit without saving");
		quit.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				//switchTo(new MainCanvas());
			}
		});
		addMenuItem(quit);
        
        JButton impBuild = new JButton("Import Builds");
        impBuild.addActionListener((e)->{
            File[] chosen = FileChooserUtil.chooseFiles();
            if(chosen != null){
                for(File f : chosen){
                    Build.loadFile(f);
                }
            }
        });
        addMenuItem(impBuild);
        
        JButton expBuild = new JButton("Export Builds");
        expBuild.addActionListener((e)->{
            File dir = FileChooserUtil.chooseDir();
            if(dir != null){
                String name = JOptionPane.showInputDialog("Enter a name for this export:");
                File buildFile = new File(dir.getAbsolutePath() + "/" + name);
                Build.saveAllToFile(buildFile);
            }
        });
        addMenuItem(expBuild);
		
		temp = new JButton("customize upgradables");
		temp.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				switchTo(new CustomizeCanvas());
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
		
		setClass = new JButton("Edit/copy build");
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
		resizeMenu(3);
	}
	public void phase2(){
		remove(temp);
		testPlayer = new Player("TEST");
		Build b = Build.getBuildByName(baseBuild.getSelected().toString());
		testPlayer.applyBuild(b);
		
		add(new JComponent(){});
		name = new JTextArea(b.getName());
		add(name);
		name.setEditable(true);
        
		classSelect = new CustomizableSelector<CharacterStatName>("Character Class", CharacterClass.getAll());
		classSelect.getBox().setSelectedName(b.getClassName());
		add(classSelect);
		
		actives = new CustomizableSelector[3];
		for(int i = 0; i < 3; i++){
			AbstractActive[] options = AbstractActive.getAll();
			actives[i] = new CustomizableSelector<ActiveStatName>("Active #" + (i + 1), options);
			actives[i].getBox().setSelectedName(b.getActiveNames()[i]);
			add(actives[i]);
		}
		
		passives = new CustomizableSelector[3];
		for(int i = 0; i < 3; i++){
			AbstractPassive[] options = AbstractPassive.getAll();
			passives[i] = new CustomizableSelector<PassiveStatName>("Passive #" + (i + 1), options);
			passives[i].getBox().setSelectedName(b.getPassiveNames()[i]);
			add(passives[i]);
		}
		
		finish = new JButton("Save");
		finish.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e){
				Build.addBuild(new Build(
						name.getText(), 
						classSelect.getBox().getSelected().getName(), 
						actives[0].getBox().getSelected().getName(), 
						actives[1].getBox().getSelected().getName(), 
						actives[2].getBox().getSelected().getName(), 
						passives[0].getBox().getSelected().getName(), 
						passives[1].getBox().getSelected().getName(), 
						passives[2].getBox().getSelected().getName()
								));
				//switchTo(new MainCanvas());
			}
		});
		addMenuItem(finish);
		
		
		//upgradableSelectors[i].getBox().setSelected(names[i % 3]);
			
		
		
		
		//TODO: add tooltip of selected item desc instead of CustomizableSelector
		
		resizeComponents(3, 3);
		resizeMenu(2);
		revalidate();
		repaint();
	}
}
