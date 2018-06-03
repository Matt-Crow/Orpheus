package entities;

import actives.AbstractActive;
import initializers.Master;

public class SeedProjectile extends Projectile{
	private boolean canExplode;
	private int useId; // the useId of the attack instance this was spawned from
	public SeedProjectile(int id, int x, int y, int degrees, int momentum, Player attackUser, AbstractActive a){
		super(id, x, y, degrees, momentum, attackUser, a);
		useId = id;
		canExplode = getAttack().getStatValue("AOE") != 0;
	}
	
	public void explode(){
		for(int i = 0; i < Master.TICKSTOROTATE; i++){
			getAttack().registerProjectile(new AOEProjectile(useId, getX(), getY(), 360 * i / Master.TICKSTOROTATE, 5, getUser(), getAttack()));
		}
		canExplode = false;
	}
	
	public void terminate(){
		super.terminate();
		if(canExplode){
			explode();
		}
	}
}
