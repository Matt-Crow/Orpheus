package initializers;
import actives.LoadActives;
import passives.LoadPassives;
import windows.StartWindow;

public class Run {
	public static void main(String[] args) {
		LoadActives.load();
		LoadPassives.load();
		new StartWindow();
	}
}
