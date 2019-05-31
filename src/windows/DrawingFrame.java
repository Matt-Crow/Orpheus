package windows;

import javax.swing.*;
import initializers.Master;
import util.CloseWindow;

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
