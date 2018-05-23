package passives;

import statuses.StatusName;

public class LoadPassives {
	public static void load(){
		OnMeleeHitPassive lh = new OnMeleeHitPassive("Leechhealer", 20, true);
		lh.addStatus(StatusName.REGENERATION, 1, 5);
		
		OnMeleeHitPassive m = new OnMeleeHitPassive("Momentum", 20, true);
		m.addStatus(StatusName.RUSH, 1, 20);
		
		OnMeleeHitPassive s = new OnMeleeHitPassive("Sharpen", 20, true);
		s.addStatus(StatusName.STRENGTH, 1, 1);
		
		OnMeleeHitPassive ss = new OnMeleeHitPassive("Sparking Strikes", 20, true);
		ss.addStatus(StatusName.CHARGE, 1, 10);
		
		OnBeHitPassive nh = new OnBeHitPassive("Nature's Healing", 100, true);
		nh.addStatus(StatusName.REGENERATION, 1, 1);
		
		OnBeHitPassive r = new OnBeHitPassive("Recover", 20, true);
		r.addStatus(StatusName.REGENERATION, 1, 5);
		
		OnBeHitPassive t = new OnBeHitPassive("Toughness", 20, true);
		t.addStatus(StatusName.RESISTANCE, 1, 1);
		
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
