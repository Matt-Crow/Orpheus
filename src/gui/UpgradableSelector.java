package gui;

import javax.swing.JComponent;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import upgradables.AbstractUpgradable;

@SuppressWarnings("serial")
public class UpgradableSelector extends JComponent{
	private OptionBox<AbstractUpgradable> box;
	private Text desc;
	
	public UpgradableSelector(String title, AbstractUpgradable[] a){
		super();
		setLayout(new GridLayout(2, 1));
		box = new OptionBox<AbstractUpgradable>(title, a);
		box.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				desc.setText(box.getSelected().getDescription());
			}
		});
		add(box);
		
		desc = new Text(box.getSelected().getDescription());
		add(desc);
		Style.applyStyling(this);
	}
	public OptionBox<AbstractUpgradable> getBox(){
		return box;
	}
}
