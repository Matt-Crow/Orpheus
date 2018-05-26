package passives;

import statuses.StatusName;

public class LoadPassives {
	public static void load(){
		OnMeleeHitPassive lh = new OnMeleeHitPassive("Leechhealer", true);
		lh.addStatus(StatusName.REGENERATION, 1, 5, 20);
		
		OnMeleeHitPassive m = new OnMeleeHitPassive("Momentum", true);
		m.addStatus(StatusName.RUSH, 1, 20, 20);
		
		OnMeleeHitPassive s = new OnMeleeHitPassive("Sharpen", true);
		s.addStatus(StatusName.STRENGTH, 1, 1, 20);
		
		OnMeleeHitPassive ss = new OnMeleeHitPassive("Sparking Strikes", true);
		ss.addStatus(StatusName.CHARGE, 1, 10, 20);
		
		OnBeHitPassive nh = new OnBeHitPassive("Nature's Healing", true);
		nh.addStatus(StatusName.REGENERATION, 1, 1);
		
		OnBeHitPassive r = new OnBeHitPassive("Recover", true);
		r.addStatus(StatusName.REGENERATION, 1, 5, 20);
		
		OnBeHitPassive t = new OnBeHitPassive("Toughness", true);
		t.addStatus(StatusName.RESISTANCE, 1, 1, 20);
		
		ThresholdPassive a = new ThresholdPassive("Adrenaline", 25);
		a.addStatus(StatusName.CHARGE, 2, 1);
		
		ThresholdPassive b = new ThresholdPassive("Bracing", 25);
		b.addStatus(StatusName.RESISTANCE, 2, 1);
		
		ThresholdPassive d = new ThresholdPassive("Determination", 25);
		d.addStatus(StatusName.STRENGTH, 1, 1);
		d.addStatus(StatusName.RESISTANCE, 1, 1);
		
		ThresholdPassive e = new ThresholdPassive("Escapist", 25);
		e.addStatus(StatusName.RUSH, 2, 1);
		
		ThresholdPassive re = new ThresholdPassive("Retaliation", 25);
		re.addStatus(StatusName.STRENGTH, 2, 1);
		
		AbstractPassive.addPassives(new AbstractPassive[]{
				lh,
				m,
				s,
				ss,
				nh,
				r,
				t,
				a,
				b,
				d,
				e,
				re
		});
	}
}
