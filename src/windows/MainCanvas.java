package windows;

import java.awt.event.ActionEvent;
import javax.swing.*;

import battle.Team;
import windows.WorldSelect.WorldSelectPage;

@SuppressWarnings("serial")
public class MainCanvas extends OldContentPage{
	
	public MainCanvas(){
		super();
		
		JButton b = new JButton("Quit");
        
        MainCanvas m = this;
        
		b.addActionListener(new AbstractAction(){
            @Override
			public void actionPerformed(ActionEvent e){
				m.switchTo(new StartCanvas());
			}
		});
		addMenuItem(b);
		
		JButton newBuild = new JButton("Customize");
		newBuild.addActionListener(new AbstractAction(){
            @Override
			public void actionPerformed(ActionEvent e){
				switchTo(new BuildCanvas());
			}
		});
		add(newBuild);

        OldContentPage p = this;
        
		JButton battle = new JButton("Battle");
		battle.addActionListener(new AbstractAction(){
            @Override
			public void actionPerformed(ActionEvent e){
				switchTo(new WorldSelectPage());
			}
		});
		add(battle);
		
		resizeComponents(1, 2);
		resizeMenu(1);
	}
}