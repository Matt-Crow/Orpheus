package gui;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import upgradables.AbstractUpgradable;

@SuppressWarnings("serial")
public class UpgradableCustomizer extends JComponent{
	private AbstractUpgradable customizing;
	private Text name;
	private Pane p1; // used to split into two sections
	private Pane p2;
	private Text desc;
	private Button save;
	private int boxCount;
	
	public UpgradableCustomizer(AbstractUpgradable a){
		super();
		customizing = a.copy();
		
		p1 = new Pane();
		p2 = new Pane();
		p2.setLayout(new GridLayout(1, 1));
		add(p1);
		add(p2);
		
		name = new Text(a.getName());
		name.setEditable(true);
		name.addCaretListener(new CaretListener(){
			public void caretUpdate(CaretEvent e){
				customizing.setName(name.getText());
				setCanSave(true);
			}
		});
		p1.add(name);
		
		desc = new Text(a.getDescription());
		p2.setLayout(new GridLayout(2, 1));
		p2.add(desc);
		
		save = new Button("Save changes");
		save.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				save();
			}
		});
		p2.add(save);
		
		boxCount = 0;
		setLayout(new GridLayout(1, 2));
		Style.applyStyling(this);
	}
	private void setCanSave(boolean b){
		if(b){
			save.setText("Save changes");
			save.setEnabled(true);
		} else {
			save.setText("No changes to save");
			save.setEnabled(false);
		}
	}
	public AbstractUpgradable getCustomizing(){
		return customizing;
	}
	@SuppressWarnings("rawtypes")
	public void addBox(OptionBox box){
		p1.add(box);
		boxCount++;
		p1.setLayout(new GridLayout(boxCount + 1, 1));
		revalidate();
		repaint();
	}
	public void addBox(String s){
		Integer[] options = new Integer[]{0, 1, 2, 3, 4, 5};
		OptionBox<Integer> box = new OptionBox<>(s, options);
		box.setSelected((Integer)customizing.getBase(s));
		box.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				updateField(box.getTitle(), box.getSelected());
			}
		});
		addBox(box);
	}
	
	public void updateField(String name, int val){
		// change a stat of customizing
		// make subclasses define this
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
