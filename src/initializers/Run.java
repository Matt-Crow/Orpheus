package initializers;
import windows.StartWindow;
import customizables.CharacterClass;

public class Run {

	public static void main(String[] args) {
		//new StartWindow();
		CharacterClass bob = new CharacterClass();
		bob.setHPData(1.0, 1.0, 1.0);
		bob.setEnergyData(1.0, 1.0, 1.0, 1.0, 1.0);
		bob.calcStats();
		bob.displayStatData();
	}
}
