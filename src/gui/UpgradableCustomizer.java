package gui;

import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import upgradables.AbstractUpgradable;

@SuppressWarnings("serial")
public class UpgradableCustomizer extends JComponent{
	private AbstractUpgradable customizing;
	private Text name;
	private ArrayList<OptionBox<Integer>> boxes;
	private Pane p1; // used to split into two sections
	private Pane p2;
	private Text desc;
	
	public UpgradableCustomizer(AbstractUpgradable a){
		super();
		customizing = a.copy();
		boxes = new ArrayList<>();
		
		p1 = new Pane();
		p2 = new Pane();
		p2.setLayout(new GridLayout(1, 1));
		add(p1);
		add(p2);
		
		name = new Text(a.getName());
		name.setEditable(true);
		p1.add(name);
		
		desc = new Text(a.getDescription());
		p2.add(desc);
		
		setLayout(new GridLayout(1, 2));
		Style.applyStyling(this);
	}
	
	public AbstractUpgradable getCustomizing(){
		return customizing;
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
		p1.add(box);
		boxes.add(box);
		p1.setLayout(new GridLayout(boxes.size() + 1, 1));
		revalidate();
		repaint();
	}
	
	public int[] getSelected(){
		int[] ret = new int[boxes.size()];
		for(int i = 0; i < ret.length; i++){
			ret[i] = boxes.get(i).getSelected();
		}
		return ret;
	}
	public ArrayList<OptionBox<Integer>> getBoxes(){
		return boxes;
	}
	
	public void updateField(String name, int val){
		// change a stat of customizing
		// make subclasses define this
		desc.setText(customizing.getDescription());
		repaint();
	}
	
	public class Pane extends JComponent{
		// use this to separate into two panels
		public Pane(){
			super();
			Style.applyStyling(this);
		}
	}
}
