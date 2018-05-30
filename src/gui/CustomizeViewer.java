package gui;

import upgradables.AbstractUpgradable;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import actives.AbstractActive;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class CustomizeViewer extends JComponent{
	// used to see changes to an upgradable as you're customizing it
	private UpgradableCustomizer customizer;
	private Text desc;
	
	public CustomizeViewer(AbstractUpgradable a){
		customizer = new UpgradableCustomizer(a);
		if(a instanceof AbstractActive){
			customizer = new ActiveCustomizer((AbstractActive)a);
		}
		for(OptionBox<Integer> box : customizer.getBoxes()){
			box.addActionListener(new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					// more here
					desc.setText(a.getDescription());
					revalidate();
					repaint();
				}
			});
		}
		add(customizer);
		
		desc = new Text(a.getDescription());
		add(desc);
		
		setLayout(new GridLayout(1, 2));
		Style.applyStyling(this);
	}
}
