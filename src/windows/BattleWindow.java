package windows;

import javax.swing.JFrame;
import resources.CloseWindow;

public class BattleWindow extends JFrame{
	public static final long serialVersionUID = 1L;
	BattleCanvas draw;
	
	public BattleWindow(int w, int h){
		setTitle("Match");
		addWindowListener(new CloseWindow());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(w, h);
		
		draw = new BattleCanvas(w, h);
		setContentPane(draw);
		setVisible(true);
	}
	public BattleCanvas getCanvas(){
		return draw;
	}
}
