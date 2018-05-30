package gui;

import java.util.ArrayList;
import javax.swing.JComponent;

import java.awt.GridLayout;

import upgradables.AbstractUpgradable;

@SuppressWarnings("serial")
public class UpgradableCustomizer extends JComponent{
	private AbstractUpgradable customizing;
	private Text name;
	private ArrayList<OptionBox<Integer>> boxes;
	
	
	public UpgradableCustomizer(AbstractUpgradable a){
		super();
		customizing = a.copy();
		boxes = new ArrayList<>();
		
		name = new Text(a.getName());
		name.setEditable(true);
		add(name);
		Style.applyStyling(this);
	}
	
	public void addBox(String s){
		Integer[] options = new Integer[]{0, 1, 2, 3, 4, 5};
		OptionBox<Integer> box = new OptionBox<>(s, options);
		box.setSelected((Integer)customizing.getBase(s));
		add(box);
		boxes.add(box);
		setLayout(new GridLayout(boxes.size() + 1, 1));
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
}
