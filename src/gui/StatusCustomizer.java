package gui;

import java.awt.GridLayout;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import statuses.AbstractStatus;
import statuses.StatusName;
import customizables.AbstractCustomizable;

//TODO: make this take only upgradable as param so that it can do chance
@SuppressWarnings("serial")
public class StatusCustomizer extends JComponent{
	private AbstractCustomizable statusOwner;
	private OptionBox<StatusName> box;
	private OptionBox<Integer> intensity;
	private OptionBox<Integer> duration;
	
	public StatusCustomizer(AbstractCustomizable a, AbstractStatus s){
		super();
		statusOwner = a;
		
		box = new OptionBox<>("Status", StatusName.values());
		box.setSelected(StatusName.valueOf(s.getName().toUpperCase()));
		add(box);
		
		Integer[] v1 = {1, 2, 3};
		intensity = new OptionBox<>("Intensity", v1);
		intensity.setSelected((Integer)s.getIntensityLevel());
		add(intensity);
		
		Integer[] v2 = {1, 2, 3};
		duration = new OptionBox<>("Duration", v2);
		duration.setSelected((Integer)s.getBaseParam());
		add(duration);
		
		setLayout(new GridLayout(3, 1));
		
		Style.applyStyling(this);
	}
	
	public void saveStatus(){
		statusOwner.addStatus(AbstractStatus.decode(box.getSelected(), intensity.getSelected(), duration.getSelected()));
	}
	public void addActionListener(AbstractAction a){
		box.addActionListener(a);
		intensity.addActionListener(a);
		duration.addActionListener(a);
	}
}
