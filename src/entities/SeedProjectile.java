package entities;

import java.util.ArrayList;

import actions.OnHitTrip;
import actions.OnHitKey;
import attacks.Attack;
import initializers.Master;
import resources.Op;

public class SeedProjectile extends Projectile{
	private boolean canExplode;
	public SeedProjectile(int x, int y, int dirNum, int momentum, Player attackUser, Attack a){
		super(x, y, dirNum, momentum, attackUser, a);
		canExplode = getAttack().getStatValue("AOE") != 0;
		OnHitKey act = new OnHitKey(){
			public void trip(OnHitTrip t){
				explode();
			}
		};
		if(canExplode){
			getActionRegister().addOnHit(act);
		}
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
		Op.add("explode count: " + aoe.size());
		Op.dp();
	}
	
	public void update(){
		super.update();
		if(getShouldTerminate()){
			if(canExplode){
				explode();
			}
		}
	}
}
