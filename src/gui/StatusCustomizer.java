package gui;

import java.awt.GridLayout;

import javax.swing.JComponent;
import statuses.AbstractStatus;
import statuses.StatusName;
import customizables.AbstractCustomizable;
import java.util.ArrayList;
import java.util.function.Consumer;

public class StatusCustomizer extends JComponent{
	private final OptionBox<StatusName> box;
	private final NumberChoiceBox intensity;
	private final NumberChoiceBox duration;
    private final ArrayList<Consumer<AbstractStatus>> statusChangedListeners;
    private AbstractStatus status;
	
	public StatusCustomizer(AbstractStatus s){
		super();
        status = s;
        statusChangedListeners = new ArrayList<>();
		
		box = new OptionBox<>("Status", StatusName.values());
		box.setSelected(StatusName.valueOf(s.getName().toUpperCase()));
		Style.applyStyling(box);
        add(box);
		
		intensity = new NumberChoiceBox("Intensity", 1, 3);
		intensity.setSelected((Integer)s.getIntensityLevel());
        Style.applyStyling(intensity);
        intensity.setSelected(s.getIntensityLevel());
		add(intensity);
		
		duration = new NumberChoiceBox("Duration", 1, 3);
		duration.setSelected((Integer)s.getBaseParam());
        Style.applyStyling(duration);
        duration.setSelected(s.getBaseParam());
		add(duration);
		
        intensity.addSelectionChangeListener((i)->{
            status = AbstractStatus.decode(box.getSelected(), intensity.getSelected(), duration.getSelected());
            statusChanged();
        });
        duration.addSelectionChangeListener((i)->{
            status = AbstractStatus.decode(box.getSelected(), intensity.getSelected(), duration.getSelected());
            statusChanged();
        });
        
		setLayout(new GridLayout(3, 1));
		
		Style.applyStyling(this);
	}
	
    public void addStatusChangedListener(Consumer<AbstractStatus> func){
        statusChangedListeners.add(func);
    }
    private void statusChanged(){
        statusChangedListeners.forEach((c)->c.accept(status));
    }
    
    public AbstractStatus getStatus(){
        return status;
    }
}
