package windows;

import javax.swing.JFrame;
import initializers.Master;
import resources.CloseWindow;

public class BattleWindow extends JFrame{
	public static final long serialVersionUID = 1L;
	BattleCanvas draw;
	
	public BattleWindow(){
		setTitle("Match");
		addWindowListener(new CloseWindow());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
		
		draw = new BattleCanvas(Master.CANVASWIDTH, Master.CANVASHEIGHT);
		setContentPane(draw);
		setVisible(true);
	}
	public BattleCanvas getCanvas(){
		return draw;
	}
}
