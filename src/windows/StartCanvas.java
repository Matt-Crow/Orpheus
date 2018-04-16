package windows;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import gui.Button;
import gui.Title;

import resources.Op;
import resources.DrawingPlane;

@SuppressWarnings("serial")
public class StartCanvas extends DrawingPlane{
	public static final long serialVersionUID = 1L;
	
	public StartCanvas(){
		super();
		
		add(new VoidComponent());
		
		Title title = new Title("The Orpheus Proposition");
		add(title);
		
		add(new VoidComponent());
		
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
				new MainWindow();
				close();
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
		resizeComponents(2, 3);
	}
}
