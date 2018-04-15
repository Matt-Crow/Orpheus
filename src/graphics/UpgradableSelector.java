package graphics;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import java.awt.GridLayout;

import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import upgradables.AbstractUpgradable;

@SuppressWarnings("serial")
public class UpgradableSelector extends JComponent{
	private OptionBox<AbstractUpgradable> box;
	private JTextArea desc;
	
	public UpgradableSelector(String title, ArrayList<AbstractUpgradable> a){
		super();
		setLayout(new GridLayout(2, 1));
		AbstractUpgradable[] opt = new AbstractUpgradable[a.size()];
		for(int i = 0; i < a.size(); i++){
			opt[i] = a.get(i);
		}
		box = new OptionBox<AbstractUpgradable>(title, opt);
		box.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				desc.setText(box.getSelected().getDescription());
			}
		});
		add(box);
		
		desc = new JTextArea(box.getSelected().getDescription());
		desc.setEditable(false);
		add(desc);
	}
	public OptionBox<AbstractUpgradable> getBox(){
		return box;
	}
}
