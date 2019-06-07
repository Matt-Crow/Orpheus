package passives;

import statuses.*;

public class LoadPassives {
	public static void load(){
		OnMeleeHitPassive lh = new OnMeleeHitPassive("Leechhealer", true);
		lh.addStatus(new Regeneration(1, 1));
		
		OnMeleeHitPassive m = new OnMeleeHitPassive("Momentum", true);
		m.addStatus(new Rush(1, 1));
		
		OnMeleeHitPassive s = new OnMeleeHitPassive("Sharpen", true);
		s.addStatus(new Strength(1, 1));
		
		OnMeleeHitPassive ss = new OnMeleeHitPassive("Sparking Strikes", true);
		ss.addStatus(new Charge(1, 1));
		
		OnBeHitPassive nh = new OnBeHitPassive("Nature's Healing", true);
		nh.addStatus(new Regeneration(1, 1));
		
		OnBeHitPassive r = new OnBeHitPassive("Recover", true);
		r.addStatus(new Regeneration(2, 1));
		
		OnBeHitPassive t = new OnBeHitPassive("Toughness", true);
		t.addStatus(new Resistance(1, 1));
		
		ThresholdPassive a = new ThresholdPassive("Adrenaline", 3);
		a.addStatus(new Charge(2, 1));
		
		ThresholdPassive b = new ThresholdPassive("Bracing", 3);
		b.addStatus(new Resistance(2, 1));
		
		ThresholdPassive d = new ThresholdPassive("Determination", 3);
		d.addStatus(new Strength(1, 1));
		d.addStatus(new Resistance(1, 1));
		
		ThresholdPassive e = new ThresholdPassive("Escapist", 3);
		e.addStatus(new Rush(2, 1));
		
		ThresholdPassive re = new ThresholdPassive("Retaliation", 3);
		re.addStatus(new Strength(2, 1));
        
        OnHitPassive rc = new OnHitPassive("Recharge", true);
        rc.addStatus(new Charge(1, 1));
		
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
				re,
                rc
		});
	}
}
