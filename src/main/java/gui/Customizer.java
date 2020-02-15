package gui;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import statuses.StatusTable;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import customizables.AbstractCustomizable;
import java.awt.BorderLayout;
import statuses.AbstractStatus;

@SuppressWarnings("serial")
public abstract class Customizer<T> extends JComponent{
	private AbstractCustomizable customizing;
	private JTextArea name;
    private final JPanel boxGroup;
	private final JTextArea desc;
	private final JButton save;
	private int boxCount;
	private final ArrayList<StatusCustomizer> statusBoxes; //need to keep track of these
	
	public Customizer(AbstractCustomizable a){
		super();
		customizing = a.copy();
		
        setLayout(new GridLayout(1, 2));
        
        
        //customize section
        JPanel customizeSection = new JPanel();
        customizeSection.setLayout(new BorderLayout());
        add(customizeSection);
        
		name = new JTextArea(customizing.getName());
		Style.applyStyling(name);
        name.setEditable(true);
		name.addCaretListener(new CaretListener(){
			public void caretUpdate(CaretEvent e){
				setCanSave(true);
			}
		});
		customizeSection.add(name, BorderLayout.PAGE_START);
        
        boxGroup = new JPanel();
        boxGroup.setLayout(new GridLayout(1, 1));
        JScrollPane scrolly = new JScrollPane(boxGroup);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        customizeSection.add(scrolly, BorderLayout.CENTER);
        
        
        
		//non-customize section
        JPanel other = new JPanel();
        other.setLayout(new BorderLayout());
		Style.applyStyling(other);
        add(other);
        
        desc = new JTextArea(customizing.getDescription());
		other.add(desc, BorderLayout.CENTER);
        Style.applyStyling(desc);
		
		save = new JButton("Save changes");
		save.addActionListener(new AbstractAction(){
            @Override
			public void actionPerformed(ActionEvent e){
				save();
			}
		});
        Style.applyStyling(save);
		other.add(save, BorderLayout.PAGE_END);
		
        statusBoxes = new ArrayList<>();
		boxCount = 0;
		Style.applyStyling(this);
	}
	private void setCanSave(boolean b){
		if(b){
			save.setText("Save changes");
			save.setEnabled(true);
			desc.setText(customizing.getDescription());
		} else {
			save.setText("No changes to save");
			save.setEnabled(false);
		}
	}
	public AbstractCustomizable getCustomizing(){
		return customizing;
	}
	
	public void addBox(JComponent j){
		Style.applyStyling(j);
        boxGroup.add(j);
		boxCount++;
		boxGroup.setLayout(new GridLayout(boxCount + 1, 1));
		revalidate();
		repaint();
	}
	public void addBox(Enum s, int minValue, int maxValue){
        NumberChoiceBox box = new NumberChoiceBox(s.toString(), minValue, maxValue);
        box.addSelectionChangeListener((i)->{
            updateField(s.toString(), i);
        });
        box.setSelected(customizing.getBase(s));
		addBox(box);
	}
    public void addStatusBox(AbstractStatus s){
        StatusCustomizer c = new StatusCustomizer(s);
        c.addStatusChangedListener((AbstractStatus as)->{
            customizing.clearInflict();
            statusBoxes.forEach((sb)->{
                customizing.addStatus(sb.getStatus());
            });
            customizing.init();
            desc.setText(customizing.getDescription());
        });
        addBox(c);
        statusBoxes.add(c);
    }
	public void addStatusBoxes(){
		StatusTable s = customizing.getInflict();
        s.forEach((status)->{
            addStatusBox(status);
        });
	}
	
	public void updateField(String name, int val){
		// change a stat of customizing
		// make subclasses define this
        customizing.init();
		desc.setText(customizing.getDescription());
		setCanSave(true);
		repaint();
	}
	
	public void save(){
		// subclasses define this
		setCanSave(false);
	}
}