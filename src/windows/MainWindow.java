package windows;

public class MainWindow extends DrawingFrame{
	/**
	 * Mainwindow is the lobby window used for the game, featuring the customizer and battle options
	 */
	
	public static final long serialVersionUID = 1L;
	MainCanvas draw;
	
	public MainWindow(){
		setTitle("The Orpheus Proposition");
		
		draw = new MainCanvas();
		setContentPane(draw);
		setVisible(true);
	}
}
