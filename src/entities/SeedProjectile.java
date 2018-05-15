package entities;

import java.util.ArrayList;

import attacks.Attack;
import initializers.Master;

public class SeedProjectile extends Projectile{
	private boolean canExplode;
	public SeedProjectile(int momentum, Player attackUser, Attack a){
		super(momentum, attackUser, a);
		canExplode = getAttack().getStatValue("AOE") != 0;
	}
	
	public void explode(){
		ArrayList<AOEProjectile> aoe = new ArrayList<>();
		for(int i = 0; i < Master.TICKSTOROTATE; i++){
			AOEProjectile p = new AOEProjectile(5, getUser(), getAttack(), getHit());
			p.init(getX(), getY(), 360 * i / Master.TICKSTOROTATE);
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
