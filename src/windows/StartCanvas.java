package windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import initializers.Master;

import javax.swing.JLabel;
import javax.swing.AbstractAction;
import gui.Button;

import resources.Op;
import resources.DrawingPlane;

@SuppressWarnings("serial")
public class StartCanvas extends DrawingPlane{
	public static final long serialVersionUID = 1L;
	
	public StartCanvas(){
		super();
		
		addComp(new VoidComponent());
		
		JLabel title = new JLabel("The Orpheus Proposition");
		title.setLayout(null);
		title.setBounds(0, 0, Master.CANVASWIDTH, Master.CANVASHEIGHT / 3);
		title.setBackground(Color.yellow);
		title.setOpaque(true);
		addComp(title);
		
		addComp(new VoidComponent());
		
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
			addComp(b);
		}
		resizeComponents(2, 3);
	}
}
