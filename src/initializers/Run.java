package initializers;
import windows.StartWindow;
import attacks.LoadActives;

public class Run {
	public static void main(String[] args) {
		LoadActives.load();
		new StartWindow();
	}
}
