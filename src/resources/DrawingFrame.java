package resources;

import javax.swing.JFrame;
import initializers.Master;
import resources.CloseWindow;

@SuppressWarnings("serial")
public class DrawingFrame extends JFrame{
	public DrawingFrame(){
		super();
		setDefaultCloseOperation(DrawingFrame.EXIT_ON_CLOSE);
		setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
		addWindowListener(new CloseWindow());
		setVisible(true);
	}
}
