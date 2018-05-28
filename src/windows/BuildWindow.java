package windows;

public class BuildWindow extends DrawingFrame{
	public static final long serialVersionUID = 4L;
	
	public BuildWindow(){
		setTitle("New Build");
		
		setContentPane(new BuildCanvas());
		setVisible(true);
	}
}
