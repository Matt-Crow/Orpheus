package windows;

@SuppressWarnings("serial")
public class CustomizeWindow extends DrawingFrame{
	public CustomizeWindow(){
		setTitle("Customizing");
		setContentPane(new CustomizeCanvas());
	}
}
