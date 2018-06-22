package initializers;
import actives.LoadActives;
import customizables.LoadCharacterClasses;
import passives.LoadPassives;
import windows.StartWindow;

public class Run {
	public static void main(String[] args) {
		LoadActives.load();
		LoadPassives.load();
		LoadCharacterClasses.load();
		new StartWindow();
	}
}
