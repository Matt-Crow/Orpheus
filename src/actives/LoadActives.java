package actives;

import entities.ParticleType;
import graphics.CustomColors;
import statuses.*;

// there's gotta be a better way to do this
public class LoadActives {
	public static void load(){
		// read from file later?
		ElementalActive bp = new ElementalActive("Blazing Pillars", 3, 5, 5, 1, 0, 1);
		bp.enableTracking();
		bp.addStatus(new Burn(1, 5), 70); // change later
		bp.setParticleType(ParticleType.BURST);
		bp.setColorBlend(CustomColors.fireColors);
		bp.setArc(360, 4); // only spawning 2-3 projectiles
		
		ElementalActive bt = new ElementalActive("Boulder Toss", 5, 5, 2, 2, 3, 4);
		bt.addStatus(new Stun(3, 4), 70);
		bt.setParticleType(ParticleType.BURST);
		bt.setColorBlend(CustomColors.earthColors);
		
		ElementalActive cd = new ElementalActive("Cursed Daggers", 2, 3, 5, 5, 0, 0);
		cd.setParticleType(ParticleType.BEAM);
		cd.setParticleColor(CustomColors.green);
		cd.setArc(90, 7);
		
		ElementalActive eq = new ElementalActive("Earthquake", 4, 5, 0, 2, 3, 0);
		eq.addStatus(new Stun(3, 3), 100);
		eq.setParticleType(ParticleType.BURST);
		eq.setColorBlend(CustomColors.earthColors);
		
		ElementalActive fof = new ElementalActive("Fields of Fire", 3, 3, 0, 5, 3, 0);
		fof.addStatus(new Burn(1, 3), 100);
		fof.setParticleType(ParticleType.SHEAR);
		fof.setColorBlend(CustomColors.fireColors);
		
		ElementalActive fb = new ElementalActive("Fireball", 4, 5, 4, 3, 3, 4);
		fb.setParticleType(ParticleType.BURST);
		
		ElementalActive mfb = new ElementalActive("Mega Firebolt", 4, 4, 3, 3, 3, 4);
		mfb.addStatus(new Burn(1, 5), 33);
		mfb.setParticleType(ParticleType.SHEAR);
		mfb.setColorBlend(CustomColors.fireColors);
		
		ElementalActive mwb = new ElementalActive("Mini Windbolt", 1, 1, 5, 5, 0, 1);
		mwb.setParticleType(ParticleType.BEAM);
		mwb.setColorBlend(CustomColors.airColors);
		
		ElementalActive rod = new ElementalActive("RAINBOW OF DOOM", 1, 5, 3, 5, 5, 1);
		rod.setParticleType(ParticleType.BURST);
		rod.setColorBlend(CustomColors.rainbow);
		
		ElementalActive stk = new ElementalActive("Summon the Kraken", 1, 1, 0, 3, 5, 1);
		stk.enableTracking();
		stk.setParticleType(ParticleType.SHEAR);
		stk.setColorBlend(CustomColors.waterColors);
		
		ElementalActive tpt = new ElementalActive("Tracking Projectile Test", 1, 1, 5, 3, 3, 3);
		tpt.enableTracking();
		
		ElementalActive wb = new ElementalActive("Waterbolt", 3, 3, 3, 3, 1, 2);
		wb.setParticleType(ParticleType.BEAM);
		wb.setColorBlend(CustomColors.waterColors);
		
		AbstractActive.addActives(new AbstractActive[]{
			bp,
			bt,
			cd,
			eq,
			fof,
			fb,
			mfb,
			mwb,
			rod,
			stk,
			tpt,
			wb
		});
		
		// remove these
		new BoostActiveBlueprint("Warrior's Stance", 1, 3, new StatusName[]{StatusName.STRENGTH, StatusName.RESISTANCE}, new int[]{1, 1}, new int[]{5, 5});
		new BoostActiveBlueprint("Speed Test", 1, 3, new StatusName[]{StatusName.RUSH}, new int[]{2}, new int[]{7});
		new BoostActiveBlueprint("Shield Stance", 1, 3, new StatusName[]{StatusName.RESISTANCE}, new int[]{2}, new int[]{7});
		new BoostActiveBlueprint("Healing Rain", 5, 5, new StatusName[]{StatusName.REGENERATION}, new int[]{1}, new int[]{90});
		new BoostActiveBlueprint("Heal", 5, 5, new StatusName[]{StatusName.HEALING}, new int[]{2}, new int[]{0});
		new BoostActiveBlueprint("Blade Stance", 1, 3, new StatusName[]{StatusName.STRENGTH}, new int[]{2}, new int[]{7});
		new BoostActiveBlueprint("Burning Rage", 3, 3, new StatusName[]{StatusName.STRENGTH, StatusName.BURN}, new int[]{3, 2}, new int[]{10, 10});
	}
}
