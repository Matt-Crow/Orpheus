package passives;

import statuses.*;

public class LoadPassives {
	public static void load(){
		OnMeleeHitPassive lh = new OnMeleeHitPassive("Leechhealer", 20, true);
		lh.addStatus(new Regeneration(1, 5));
		
		OnMeleeHitPassive m = new OnMeleeHitPassive("Momentum", 20, true);
		m.addStatus(new Rush(1, 20));
		
		OnMeleeHitPassive s = new OnMeleeHitPassive("Sharpen", 20, true);
		s.addStatus(new Strength(1, 1));
		
		OnMeleeHitPassive ss = new OnMeleeHitPassive("Sparking Strikes", 20, true);
		ss.addStatus(new Charge(1, 10));
		
		OnBeHitPassive nh = new OnBeHitPassive("Nature's Healing", 100, true);
		nh.addStatus(new Regeneration(1, 1));
		
		OnBeHitPassive r = new OnBeHitPassive("Recover", 20, true);
		r.addStatus(new Regeneration(1, 5));
		
		OnBeHitPassive t = new OnBeHitPassive("Toughness", 20, true);
		t.addStatus(new Resistance(1, 1));
		
		ThresholdPassive a = new ThresholdPassive("Adrenaline", 25);
		a.addStatus(new Charge(2, 1));
		
		ThresholdPassive b = new ThresholdPassive("Bracing", 25);
		b.addStatus(new Resistance(2, 1));
		
		ThresholdPassive d = new ThresholdPassive("Determination", 25);
		d.addStatus(new Strength(1, 1));
		d.addStatus(new Resistance(1, 1));
		
		ThresholdPassive e = new ThresholdPassive("Escapist", 25);
		e.addStatus(new Rush(2, 1));
		
		ThresholdPassive re = new ThresholdPassive("Retaliation", 25);
		re.addStatus(new Strength(2, 1));
		
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
