package actives;

import entities.ParticleType;
import statuses.*;

public class LoadActives {
	public static void load(){
		// read from file later?
		MeleeActive hs = new MeleeActive("Heavy Stroke", 4);
		MeleeActive s = new MeleeActive("Slash", 2);
		
		ElementalActive bt = new ElementalActive("Boulder Toss", 1, 2, 2, 3, 4);
		bt.setParticleType(ParticleType.BURST);
        bt.addTag(ActiveTag.KNOCKSBACK);
		
		ElementalActive cd = new ElementalActive("Cursed Daggers", 2, 5, 5, 0, 0);
		cd.setParticleType(ParticleType.BEAM);
		
		ElementalActive eq = new ElementalActive("Earthquake", 1, 0, 2, 3, 2);
		eq.setParticleType(ParticleType.BURST);
        eq.addTag(ActiveTag.KNOCKSBACK);
		
		ElementalActive fof = new ElementalActive("Fields of Fire", 1, 0, 5, 3, 2);
		fof.setParticleType(ParticleType.SHEAR);
		
		ElementalActive fb = new ElementalActive("Fireball", 2, 3, 3, 3, 4);
		fb.setParticleType(ParticleType.BURST);
		
		ElementalActive mfb = new ElementalActive("Mega Firebolt", 1, 3, 3, 3, 4);
		mfb.setParticleType(ParticleType.SHEAR);
		
		ElementalActive mwb = new ElementalActive("Mini Windbolt", 2, 5, 5, 0, 1);
		mwb.setParticleType(ParticleType.BEAM);
		
		ElementalActive rod = new ElementalActive("RAINBOW OF DOOM", 4, 3, 5, 5, 1);
		rod.setParticleType(ParticleType.BURST);
		
		ElementalActive wb = new ElementalActive("Waterbolt", 1, 3, 3, 1, 2);
		wb.setParticleType(ParticleType.BEAM);
		
		BoostActive ws = new BoostActive("Warrior's Stance", new AbstractStatus[]{new Strength(1, 2), new Resistance(1, 2)});
		BoostActive st = new BoostActive("Speed Test", new AbstractStatus[]{new Rush(2, 3)});
		BoostActive ss = new BoostActive("Shield Stance", new AbstractStatus[]{new Resistance(2, 3)});
		BoostActive hr = new BoostActive("Healing Rain", new AbstractStatus[]{new Regeneration(2, 3)});
		BoostActive bs = new BoostActive("Blade Stance", new AbstractStatus[]{new Strength(2, 3)});
		
		AbstractActive.addActives(new AbstractActive[]{
			s,
			hs,
			bt,
			cd,
			eq,
			fof,
			fb,
			mfb,
			mwb,
			rod,
			wb,
			ws,
			st,
			ss,
			hr,
			bs
		});
	}
}
