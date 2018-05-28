package gui;

import javax.swing.JComponent;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import upgradables.AbstractUpgradable;

@SuppressWarnings("serial")
public class UpgradableCustomizer extends JComponent{
	private AbstractUpgradable customizing;
	private Text desc;
	
	public UpgradableCustomizer(AbstractUpgradable a){
		super();
		customizing = a.copy();
		add(new Title(a.getName()));
		desc = new Text(a.getDescription());
		add(desc);
		for(String n : a.getStats().keySet()){
			Integer[] opt = new Integer[]{1, 2, 3, 4, 5}; // avoid pass by ref
			OptionBox<Integer> box = new OptionBox<>(n, opt);
			box.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					desc.setText(customizing.getDescription());
				}
			});
			add(box);
		}
		setLayout(new GridLayout(a.getStats().keySet().size() / 2 + 1, 2));
		Style.applyStyling(this);
	}
}
