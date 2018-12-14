package windows;

public class StartWindow  extends DrawingFrame{
	/**
	 * Startwindow is used as a 'launcher' for the game
	 */
	public static final long serialVersionUID = 1L;
	
	public StartWindow(){
		super();
		setTitle("The Orpheus Proposition");
		setContentPane(new StartCanvas());
		setVisible(true);
	}
}
