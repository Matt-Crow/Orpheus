package initializers;
import actives.AbstractActive;
import actives.LoadActives;
import customizables.LoadCharacterClasses;
import passives.LoadPassives;
import windows.StartWindow;

public class Run {
	public static void main(String[] args) {
		LoadActives.load();
        //AbstractActive.logAllPsuedoJson();
        //AbstractActive.readFile(Run.class.getClass().getResourceAsStream("/actives.csv"));
        
		LoadPassives.load();
		LoadCharacterClasses.load();
		new StartWindow();
	}
}
