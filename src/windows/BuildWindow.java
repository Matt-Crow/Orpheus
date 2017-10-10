package windows;

import javax.swing.JFrame;
import initializers.Master;

public class BuildWindow extends JFrame{
	public static final long serialVersionUID = 4L;
	
	public BuildWindow(){
		setTitle("New Build");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
		setContentPane(new BuildCanvas(Master.CANVASWIDTH, Master.CANVASHEIGHT));
		setVisible(true);
	}
}
