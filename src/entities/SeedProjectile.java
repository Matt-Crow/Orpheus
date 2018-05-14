package entities;

import java.util.ArrayList;

import attacks.Attack;
import initializers.Master;

public class SeedProjectile extends Projectile{
	private boolean canExplode;
	public SeedProjectile(int x, int y, int dirNum, int momentum, Player attackUser, Attack a){
		super(x, y, dirNum, momentum, attackUser, a);
		canExplode = getAttack().getStatValue("AOE") != 0;
	}
	
	public void explode(){
		ArrayList<AOEProjectile> aoe = new ArrayList<>();
		for(int i = 0; i < Master.TICKSTOROTATE; i++){
			aoe.add(new AOEProjectile(getX(), getY(), 360 * i / Master.TICKSTOROTATE, 5, getUser(), getAttack(), getHit()));
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
