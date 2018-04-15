package graphics;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.util.ArrayList;

import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class OptionBox<T> extends JComponent{
	private ArrayList<T> options;
	private JTextArea title;
	private JComboBox<String> box;
	
	public OptionBox(String t, T[] opt){
		super();
		setLayout(new GridBagLayout());
		
		title = new JTextArea(t);
		title.setEditable(false);
		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.BOTH;
		add(title, c1);
		
		options = new ArrayList<T>();
		for(int i = 0; i < opt.length; i++){
			options.add(opt[i]);
		}
		box = new JComboBox<>(getOptions());
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;
		c2.gridy = 1;
		c2.gridheight = 2;
		c2.fill = GridBagConstraints.BOTH;
		add(box, c2);
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
