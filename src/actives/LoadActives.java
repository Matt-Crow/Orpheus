package actives;

import statuses.*;

// there's gotta be a better way to do this
public class LoadActives {
	public static void load(){
		AbstractActive.addActives(new AbstractActive[]{
			new ElementalActive();
		});
		new BoostActiveBlueprint("Warrior's Stance", 1, 3, new StatusName[]{StatusName.STRENGTH, StatusName.RESISTANCE}, new int[]{1, 1}, new int[]{5, 5});
		new BoostActiveBlueprint("Speed Test", 1, 3, new StatusName[]{StatusName.RUSH}, new int[]{2}, new int[]{7});
		new BoostActiveBlueprint("Shield Stance", 1, 3, new StatusName[]{StatusName.RESISTANCE}, new int[]{2}, new int[]{7});
		new BoostActiveBlueprint("Healing Rain", 5, 5, new StatusName[]{StatusName.REGENERATION}, new int[]{1}, new int[]{90});
		new BoostActiveBlueprint("Heal", 5, 5, new StatusName[]{StatusName.HEALING}, new int[]{2}, new int[]{0});
		new BoostActiveBlueprint("Blade Stance", 1, 3, new StatusName[]{StatusName.STRENGTH}, new int[]{2}, new int[]{7});
		new BoostActiveBlueprint("Burning Rage", 3, 3, new StatusName[]{StatusName.STRENGTH, StatusName.BURN}, new int[]{3, 2}, new int[]{10, 10});
	}
}
