package initializers;
import actives.LoadActives;
import windows.StartWindow;

public class Run {
	public static void main(String[] args) {
		LoadActives.load();
		new StartWindow();
	}
}
