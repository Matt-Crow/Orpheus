package windows;

import javax.swing.JFrame;

public class StartWindow  extends JFrame{
	public static final long serialVersionUID = 1L;
	StartCanvas draw;
	
	public StartWindow(){
		setTitle("Orpheus");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 1000);
		
		draw = new StartCanvas();
		setContentPane(draw);
		setVisible(true);
	}
}
