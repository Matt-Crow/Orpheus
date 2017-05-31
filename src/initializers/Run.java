package initializers;
import windows.StartWindow;
import customizables.Warrior;

public class Run {

	public static void main(String[] args) {
		//new StartWindow();
		Warrior bob = new Warrior();
		bob.calcStats();
		bob.displayStatData();
	}
}
