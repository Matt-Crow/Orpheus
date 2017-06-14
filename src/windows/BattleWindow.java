package windows;

import javax.swing.JFrame;

public class BattleWindow extends JFrame{
	public static final long serialVersionUID = 1L;
	BattleCanvas draw;
	
	public BattleWindow(int w, int h){
		setTitle("Match");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(w, h);
		
		draw = new BattleCanvas(w, h);
		setContentPane(draw);
		setVisible(true);
	}
}
