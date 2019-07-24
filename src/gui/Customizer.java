package gui;

import controllers.Master;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import statuses.StatusTable;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import customizables.AbstractCustomizable;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;

//this is aweful: redo it
@SuppressWarnings("serial")
public class Customizer<T> extends JComponent{
	private AbstractCustomizable customizing;
	private JTextArea name;
    private final JPanel boxGroup;
	private JTextArea desc;
	private JButton save;
	private int boxCount;
	private ArrayList<StatusCustomizer> statusBoxes;
	
	public Customizer(AbstractCustomizable a){
		super();
		customizing = a.copy();
        customizing.setUser(Master.getUser().getPlayer());
		
        setLayout(new GridLayout(1, 2));
        
        
        //customize section
        JPanel customizeSection = new JPanel();
        customizeSection.setLayout(new BorderLayout());
        add(customizeSection);
        
		name = new JTextArea(customizing.getName());
		name.setEditable(true);
		name.addCaretListener(new CaretListener(){
			public void caretUpdate(CaretEvent e){
				customizing.setName(name.getText());
				setCanSave(true);
			}
		});
        Style.applyStyling(name);
		customizeSection.add(name, BorderLayout.PAGE_START);
        
        boxGroup = new JPanel();
        boxGroup.setLayout(new GridLayout(1, 1));
        JScrollPane scrolly = new JScrollPane(boxGroup);
        customizeSection.add(scrolly, BorderLayout.CENTER);
        
        
        
		//non-customize section
		desc = new JTextArea(customizing.getDescription());
		add(desc);
        Style.applyStyling(desc);
		
		save = new JButton("Save changes");
		save.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				save();
			}
		});
        Style.applyStyling(save);
		add(save);
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
        JPanel box = new JPanel();
        box.setLayout(new GridLayout(2, 1));
        
        JLabel l = new JLabel(s.toString());
        Style.applyStyling(l);
        box.add(l);
        
        JPanel radButtons = new JPanel();
        Style.applyStyling(radButtons);
        box.add(radButtons);
        ButtonGroup bg = new ButtonGroup();
        for(int i = minValue; i <= maxValue; i++){
            JRadioButton
            rad = new JRadioButton("" + i);
            rad.addActionListener((ActionEvent e) -> {
                updateField(s.toString(), Integer.parseInt(rad.getText()));
            });
            bg.add(rad);
            radButtons.add(rad);
        }
        radButtons.setLayout(new GridLayout(1, maxValue - minValue + 1));
		
		addBox(box);
	}
	public void addStatusBoxes(){
		StatusTable s = customizing.getInflict();
		for(int i = 0; i < s.getSize(); i++){
			StatusCustomizer c = new StatusCustomizer(customizing, s.getStatusAt(i));
			c.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					updateStatuses();
				}
			});
			boxGroup.add(c);
			statusBoxes.add(c);
			boxCount++;
		}
		boxGroup.setLayout(new GridLayout(boxCount + 1, 1));
	}
	
	public void updateField(String name, int val){
		// change a stat of customizing
		// make subclasses define this
		desc.setText(customizing.getDescription());
		setCanSave(true);
		repaint();
	}
	public void updateStatuses(){
		if(statusBoxes.size() != 0){
			customizing.clearInflict();
		}
		for(StatusCustomizer s : statusBoxes){
			s.saveStatus();
		}
		desc.setText(customizing.getDescription());
		setCanSave(true);
		repaint();
	}
	
	public void save(){
		// subclasses define this
		setCanSave(false);
	}
}