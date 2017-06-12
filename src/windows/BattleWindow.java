package windows;

import javax.swing.JFrame;

public class BattleWindow extends JFrame{
	public static final long serialVersionUID = 1L;
	BattleCanvas draw;
	
	public BattleWindow(){
		setTitle("Match");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 1000);
		
		draw = new BattleCanvas(30, 30, 100);
		setContentPane(draw);
		setVisible(true);
	}
}
