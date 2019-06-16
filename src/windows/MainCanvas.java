package windows;

import java.awt.event.ActionEvent;
import javax.swing.*;

import battle.Team;
import windows.WorldSelect.WorldSelectCanvas;

@SuppressWarnings("serial")
public class MainCanvas extends DrawingPlane{
	
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

        DrawingPlane p = this;
        
		JButton battle = new JButton("Battle");
		battle.addActionListener(new AbstractAction(){
            @Override
			public void actionPerformed(ActionEvent e){
				switchTo(new WorldSelectCanvas());
			}
		});
		add(battle);
		
		resizeComponents(1, 2);
		resizeMenu(1);
	}
}