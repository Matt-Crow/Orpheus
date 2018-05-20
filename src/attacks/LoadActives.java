package attacks;

import statuses.*;

// there's gotta be a better way to do this
public class LoadActives {
	public static void load(){
		// will need to do copy constructor somewhere
		BoostActive ws = new BoostActive("Warrior's Stance", 1, 3, new Strength(1, 5));
		ws.addStatus(new Resistance(1, 5));
		Active.addActive(ws);
		Active.addActive(new BoostActive("Speed Test", 1, 3, new Rush(2, 7)));
		Active.addActive(new BoostActive("Shield Stance", 1, 3, new Resistance(2, 7)));
		Active.addActive(new BoostActive("Healing Rain", 5, 5, new Regeneration(1, 90)));
		Active.addActive(new BoostActive("Heal", 5, 5, new Healing(2)));
		Active.addActive(new BoostActive("Blade Stance", 1, 3, new Strength(2, 7)));
		BoostActive br = new BoostActive("Burning Rage", 3, 3, new Strength(3, 10));
		br.addStatus(new Burn(2, 10));
		Active.addActive(br);
	}
}
