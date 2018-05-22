package gui;

import javax.swing.JComponent;

import java.awt.GridLayout;

import java.util.ArrayList;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class OptionBox<T> extends JComponent{
	private ArrayList<T> options;
	private Title title;
	private Dropdown<String> box;
	
	public OptionBox(String t, T[] opt){
		super();
		Style.applyStyling(this);
		setLayout(new GridLayout(2, 1));
		
		title = new Title(t);
		add(title);
		
		options = new ArrayList<T>();
		for(int i = 0; i < opt.length; i++){
			options.add(opt[i]);
		}
		box = new Dropdown<>(getOptions());
		add(box);
	}
	public String[] getOptions(){
		String[] ret = new String[options.size()];
		for(int i = 0; i < options.size(); i++){
			ret[i] = options.get(i).toString();
		}
		return ret;
	}
	public void setSelected(T item){
		box.setSelectedItem(item);
	}
	public void setSelected(int i){
		box.setSelectedIndex(i);
	}
	public void setSelected(String s){
		boolean found = false;
		String[] names = getOptions();
		for(int i = 0; i < names.length && !found; i++){
			if(names[i].equals(s)){
				found = true;
				setSelected(i);
			}
		}
	}
	public T getSelected(){
		return options.get(box.getSelectedIndex());
	}
	public void addActionListener(AbstractAction a){
		box.addActionListener(a);
	}
}
