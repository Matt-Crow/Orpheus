package windows;

import resources.DrawingFrame;

public class BattleWindow extends DrawingFrame{
	public static final long serialVersionUID = 1L;
	BattleCanvas draw;
	
	public BattleWindow(){
		setTitle("Match");
		
		draw = new BattleCanvas();
		setContentPane(draw);
	}
	public BattleCanvas getCanvas(){
		return draw;
	}
}
