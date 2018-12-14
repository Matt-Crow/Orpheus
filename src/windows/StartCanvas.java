package windows;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import gui.*;
import resources.Op;

@SuppressWarnings("serial")
public class StartCanvas extends DrawingPlane{
	public static final long serialVersionUID = 1L;
	
	public StartCanvas(){
		super();
		StartCanvas c = this;
		Title title = new Title("The Orpheus Proposition");
		addMenuItem(title);
		
		String[] buttonTexts = {"About this game", "Play", "How to Play"};
		AbstractAction[] actions = new AbstractAction[3];
		actions[0] = new AbstractAction("About this game"){
			public void actionPerformed(ActionEvent e){
				Op.add("about");
				Op.dp();
			}
		};
		actions[1] = new AbstractAction("Play"){
			public void actionPerformed(ActionEvent e){
                switchTo(new MainCanvas());
			}
		};
		actions[2] = new AbstractAction("How to play"){
			public void actionPerformed(ActionEvent e){
				Op.add("how");
				Op.dp();
			}
		};
		
		for(int i = 0; i < 3; i++){
			Button b = new Button(buttonTexts[i]);
			b.addActionListener(actions[i]);
			add(b);
		}
		resizeComponents(1, 3);
		resizeMenu(1);
	}
}
