package windows;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import actives.AbstractActive;
import passives.AbstractPassive;
import resources.Op;
import upgradables.AbstractUpgradable;
import gui.*;

@SuppressWarnings("serial")

// looks like I'll have to do seperate active and passive customizers
public class CustomizeCanvas extends DrawingPlane{
	
	private OptionBox<String> upgradableName;
	
	public CustomizeCanvas(){
		super();
		
		Button quit = new Button("Quit without saving");
		quit.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new BuildWindow();
				close();
			}
		});
		add(quit);
		
		AbstractActive[] acts = AbstractActive.getAll();
		AbstractPassive[] pass = AbstractPassive.getAll();
		String[] names = new String[acts.length + pass.length];
		int namesIndex = 0;
		int stopAt = acts.length;
		while(namesIndex < names.length){
			if(namesIndex < stopAt){
				names[namesIndex] = acts[namesIndex].getName();
			} else {
				names[namesIndex] = pass[namesIndex - stopAt].getName();
			}
			namesIndex++;
		}
		
		upgradableName = new OptionBox<>("Select upgradable to customize", names);
		add(upgradableName);
		
		Button selectBuild = new Button("Customize selected build");
		selectBuild.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				try{
					AbstractUpgradable c = AbstractActive.getActiveByName(upgradableName.getSelected());
					add(new UpgradableCustomizer(c));
				} catch(NullPointerException ex){
					AbstractUpgradable c = AbstractPassive.getPassiveByName(upgradableName.getSelected());
					add(new UpgradableCustomizer(c));
				}
				selectBuild.setEnabled(false);
				resizeComponents(1, 4);
				revalidate();
				repaint();
			}
		});
		add(selectBuild);
		
		resizeComponents(1, 3);
	}
}
