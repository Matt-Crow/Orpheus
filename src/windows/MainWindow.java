package windows;

import javax.swing.JFrame;

public class MainWindow extends JFrame{
	public static final long serialVersionUID = 1L;
	MainCanvas draw;
	
	public MainWindow(){
		setTitle("The Orpheus Proposition");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 1000);
		
		draw = new MainCanvas();
		setContentPane(draw);
		setVisible(true);
	}
}
