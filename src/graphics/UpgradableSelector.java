package graphics;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import resources.Op;

import javax.swing.JComboBox;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import upgradables.AbstractUpgradable;

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
	public void paint(Graphics g){
		optionBox.paint(g);
		desc.paint(g);
		Op.add("invoked");
		Op.dp();
	}
}
