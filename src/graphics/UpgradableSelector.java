package graphics;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import javax.swing.JComboBox;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import upgradables.AbstractUpgradable;

import resources.Op;

@SuppressWarnings("serial")
public class UpgradableSelector extends JComponent{
	private ArrayList<AbstractUpgradable> options;
	private String[] namesList;
	private JComboBox<String> optionBox;
	private JTextArea desc;
	
	public UpgradableSelector(ArrayList<AbstractUpgradable> a){
		super();
		options = a;
		namesList = new String[options.size()];
		for(int i = 0; i < a.size(); i++){
			namesList[i] = a.get(i).getName();
		}
		optionBox = new JComboBox<>(namesList);
		optionBox.setBounds(getX(), getY(), getWidth(), getHeight() / 2);
		optionBox.setVisible(true);
		optionBox.setOpaque(true);
		optionBox.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				desc.setText(options.get(optionBox.getSelectedIndex()).getDescription());
			}
		});
		
		desc = new JTextArea(options.get(0).getDescription());
		desc.setBounds(getX(), getY() + getHeight() / 2, getWidth(), getHeight() / 2);
		desc.setVisible(true);
		desc.setOpaque(true);
	}
	public void setSelected(int i){
		optionBox.setSelectedIndex(i);
	}
	public void setSelected(String s){
		for(int i = 0; i < namesList.length; i++){
			if(namesList[i] == s){
				optionBox.setSelectedIndex(i);
			}
		}
	}
	public String getSelString(){
		return optionBox.getItemAt(optionBox.getSelectedIndex());
	}
	
	public void paint(Graphics g){
		super.paint(g);
		optionBox.setBounds(getX(), getY(), getWidth(), getHeight() / 2); // not rendering at proper place
		optionBox.paint(g);
		desc.setBounds(getX(), getY() + getHeight() / 2, getWidth(), getHeight() / 2);
		desc.paint(g);
		Op.add(optionBox.getY());
		Op.add(desc.getY());
		Op.dp();
	}
}
