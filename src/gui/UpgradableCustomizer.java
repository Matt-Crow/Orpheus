package gui;

import java.util.ArrayList;
import javax.swing.JComponent;
import java.awt.GridLayout;

import upgradables.AbstractUpgradable;

@SuppressWarnings("serial")
public class UpgradableCustomizer extends JComponent{
	private AbstractUpgradable customizing;
	private ArrayList<OptionBox<Integer>> boxes;
	
	public UpgradableCustomizer(AbstractUpgradable a){
		super();
		customizing = a.copy();
		boxes = new ArrayList<>();
		add(new Title(a.getName()));
		for(String n : a.getStats().keySet()){
			Integer[] opt = new Integer[]{1, 2, 3, 4, 5}; // avoid pass by ref
			OptionBox<Integer> box = new OptionBox<>(n, opt);
			add(box);
			boxes.add(box);
		}
		setLayout(new GridLayout(a.getStats().keySet().size() + 1, 1));
		Style.applyStyling(this);
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
