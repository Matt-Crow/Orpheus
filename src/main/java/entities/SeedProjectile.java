package entities;

import util.Settings;
import customizables.actives.ElementalActive;

public class SeedProjectile extends Projectile{
	private boolean canExplode;
	private final int useId; // the useId of the attack instance this was spawned from
	public SeedProjectile(int id, int x, int y, int degrees, int momentum, AbstractPlayer attackUser, ElementalActive a){
		super(id, x, y, degrees, momentum, attackUser, a);
		useId = id;
		canExplode = getAttack().getAOE() != 0;
	}
	
	public void explode(){
		for(int i = 0; i < Settings.TICKSTOROTATE; i++){
			getAttack().getUser().spawn(new AOEProjectile(useId, getX(), getY(), 360 * i / Settings.TICKSTOROTATE, 5, getUser(), getAttack()));
        }
		canExplode = false;
	}
	
    @Override
	public void terminate(){
		super.terminate();
		if(canExplode){
			explode();
		}
	}
}
