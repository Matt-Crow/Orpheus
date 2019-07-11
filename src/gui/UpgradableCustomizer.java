package gui;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import statuses.StatusTable;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import customizables.AbstractCustomizable;

@SuppressWarnings("serial")
public class UpgradableCustomizer<T> extends JComponent{
	private AbstractCustomizable customizing;
	private JTextArea name;
	private Pane p1; // used to split into two sections
	private Pane p2;
	private JTextArea desc;
	private JButton save;
	private int boxCount;
	private ArrayList<StatusCustomizer> statusBoxes;
	
	public UpgradableCustomizer(AbstractCustomizable a){
		super();
		customizing = a.copy();
		
		p1 = new Pane();
		p2 = new Pane();
		p2.setLayout(new GridLayout(1, 1));
		add(p1);
		add(p2);
		
		name = new JTextArea(customizing.getName());
		name.setEditable(true);
		name.addCaretListener(new CaretListener(){
			public void caretUpdate(CaretEvent e){
				customizing.setName(name.getText());
				setCanSave(true);
			}
		});
		p1.add(name);
        Style.applyStyling(name);
        name.setEditable(true);
		
		desc = new JTextArea(customizing.getDescription());
		p2.setLayout(new GridLayout(2, 1));
		p2.add(desc);
        Style.applyStyling(desc);
		
		save = new JButton("Save changes");
		save.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				save();
			}
		});
        Style.applyStyling(save);
		p2.add(save);
		statusBoxes = new ArrayList<>();
		boxCount = 0;
		setLayout(new GridLayout(1, 2));
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
	
	public void addBox(OptionBox box){
		p1.add(box);
		boxCount++;
		p1.setLayout(new GridLayout(boxCount + 1, 1));
		revalidate();
		repaint();
	}
	public void addBox(Enum s){
		Integer[] options = new Integer[]{0, 1, 2, 3, 4, 5};
		OptionBox<Integer> box = new OptionBox<>(s.toString(), options);
		box.setSelected((Integer)customizing.getBase(s));
		box.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				updateField(box.getTitle(), box.getSelected());
			}
		});
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
			p1.add(c);
			statusBoxes.add(c);
			boxCount++;
		}
		p1.setLayout(new GridLayout(boxCount + 1, 1));
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
	
	public class Pane extends JComponent{
		// use this to separate into two panels
		public Pane(){
			super();
			Style.applyStyling(this);
		}
	}
}
