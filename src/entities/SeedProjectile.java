package entities;

import java.util.ArrayList;

import actives.Active;
import initializers.Master;

public class SeedProjectile extends Projectile{
	private boolean canExplode;
	public SeedProjectile(int x, int y, int degrees, int momentum, Player attackUser, Active a){
		super(x, y, degrees, momentum, attackUser, a);
		canExplode = getAttack().getStatValue("AOE") != 0;
	}
	
	public void explode(){
		ArrayList<AOEProjectile> aoe = new ArrayList<>();
		for(int i = 0; i < Master.TICKSTOROTATE; i++){
			AOEProjectile p = new AOEProjectile(getX(), getY(), 360 * i / Master.TICKSTOROTATE, 5, getUser(), getAttack(), getHit());
			aoe.add(p);
		}
		for(AOEProjectile p : aoe){
			p.getActionRegister().addOnHit(getAttack().getStatusInfliction());
			for(AOEProjectile possibleBrother : aoe){
				if(p != possibleBrother){
					p.addBrother(possibleBrother);
				}
			}
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
