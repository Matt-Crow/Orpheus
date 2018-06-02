package gui;

import javax.swing.JComponent;

import statuses.StatusName;

@SuppressWarnings("serial")
public class StatusCustomizer extends JComponent{
	private OptionBox<StatusName> box;
	private OptionBox<Integer> intensity;
	private OptionBox<Integer> duration;
	
	public StatusCustomizer(){
		super();
		box = new OptionBox<>("Status", StatusName.values());
		add(box);
		
		Integer[] v1 = {1, 2, 3};
		intensity = new OptionBox<>("Intensity", v1);
		add(intensity);
		
		Integer[] v2 = {1, 2, 3};
		duration = new OptionBox<>("Duration", v2);
		add(duration);
		
		
	}
}
