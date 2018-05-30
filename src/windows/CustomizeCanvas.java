package windows;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import actives.AbstractActive;
import passives.AbstractPassive;
import upgradables.AbstractUpgradable;
import gui.*;

@SuppressWarnings("serial")

// looks like I'll have to do seperate active and passive customizers
public class CustomizeCanvas extends DrawingPlane{
	
	private OptionBox<String> upgradableName;
	private AbstractUpgradable customizing;
	
	// used to choose the type of what to customize
	private Button act;
	private Button cha;
	private Button pas;
	
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
		
		Title t = new Title("Select what you want to customize");
		add(t);
		
		add(new VoidComponent());
		
		act = new Button("Active");
		act.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				phase2("Active");
			}
		});
		add(act);
		
		cha = new Button("Character Class");
		cha.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				
			}
		});
		add(cha);
		
		pas = new Button("Passive");
		pas.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				phase2("Passive");
			}
		});
		add(pas);
		resizeComponents(2, 3);
	}
	private void phase2(String type){
		remove(act);
		remove(cha);
		remove(pas);
		
		String[] names = new String[]{"An error occurred in CustomizeCanvas.phase2..."};
		Button customize = new Button("Customize selected build");
		add(customize);
		
		if(type.equals("Active")){
			AbstractActive[] acts = AbstractActive.getAll();
			names = new String[acts.length];
			for(int i = 0; i < acts.length; i++){
				names[i] = acts[i].getName();
			}
			customize.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					customizing = AbstractActive.getActiveByName(upgradableName.getSelected());
					remove(customize);
					remove(upgradableName);
					add(new ActiveCustomizer((AbstractActive)customizing));
					revalidate();
					repaint();
				}
			});
		} else if (type.equals("Class")){
			// implement later
		} else {
			AbstractPassive[] pass = AbstractPassive.getAll();
			names = new String[pass.length];
			for(int i = 0; i < pass.length; i++){
				names[i] = pass[i].getName();
			}
			customize.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					customizing = AbstractPassive.getPassiveByName(upgradableName.getSelected());
					remove(customize);
					remove(upgradableName);
					//add(new CustomizeViewer(customizing));
					revalidate();
					repaint();
				}
			});
		}
		upgradableName = new OptionBox<>("Select active to customize", names);
		add(upgradableName);
		resizeComponents(2, 2);
		revalidate();
		repaint();
	}
}
