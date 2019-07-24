package gui;

import java.awt.GridLayout;

import javax.swing.JComponent;
import statuses.AbstractStatus;
import statuses.StatusName;
import java.util.ArrayList;
import java.util.function.Consumer;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class StatusCustomizer extends JComponent{
	private final JComboBox<String> chooser;
	private final NumberChoiceBox intensity;
	private final NumberChoiceBox duration;
    private final ArrayList<Consumer<AbstractStatus>> statusChangedListeners;
    private AbstractStatus status;
	
	public StatusCustomizer(AbstractStatus s){
		super();
        status = s;
        statusChangedListeners = new ArrayList<>();
        
        setLayout(new GridLayout(4, 1));
        
        JLabel title = new JLabel("Status");
        Style.applyStyling(title);
        add(title);
        
        chooser = new JComboBox<>();
		Style.applyStyling(chooser);
        for(StatusName sn : StatusName.values()){
            chooser.addItem(sn.toString());
        }
        chooser.setSelectedItem(s.getName());
        chooser.addActionListener((e)->{
            updateStatus();
        });
        add(chooser);
		
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
            updateStatus();
        });
        duration.addSelectionChangeListener((i)->{
            updateStatus();
        });
		
		Style.applyStyling(this);
	}
	
    public void addStatusChangedListener(Consumer<AbstractStatus> func){
        statusChangedListeners.add(func);
    }
    private void updateStatus(){
        status = AbstractStatus.decode(StatusName.fromName((String) chooser.getSelectedItem()), intensity.getSelected(), duration.getSelected());
        statusChangedListeners.forEach((c)->c.accept(status));
    }
    
    public AbstractStatus getStatus(){
        return status;
    }
}
