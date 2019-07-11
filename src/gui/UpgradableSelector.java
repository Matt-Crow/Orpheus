package gui;

import javax.swing.JComponent;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import javax.swing.*;
import customizables.AbstractCustomizable;

@SuppressWarnings("serial")
public class UpgradableSelector<T> extends JComponent{
	private OptionBox<AbstractCustomizable> box;
	private JTextArea desc;
	
	public UpgradableSelector(String title, AbstractCustomizable[] a){
		super();
		setLayout(new GridLayout(2, 1));
		box = new OptionBox<AbstractCustomizable>(title, a);
		box.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				desc.setText(box.getSelected().getDescription());
			}
		});
		add(box);
		
		desc = new JTextArea(box.getSelected().getDescription());
		add(desc);
        Style.applyStyling(desc);
		Style.applyStyling(this);
	}
	public OptionBox<AbstractCustomizable> getBox(){
		return box;
	}
}
