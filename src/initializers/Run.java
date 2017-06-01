package initializers;
import windows.StartWindow;
import customizables.Earth;

public class Run {

	public static void main(String[] args) {
		new StartWindow();
		Earth bob = new Earth();
		bob.calcStats();
		bob.displayStatData();
	}
}
