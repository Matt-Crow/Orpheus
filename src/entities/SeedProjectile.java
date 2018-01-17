package entities;

import java.util.ArrayList;
import attacks.Attack;
import resources.OnHitAction;
import initializers.Master;

public class SeedProjectile extends Projectile{
	public SeedProjectile(int x, int y, int dirNum, int momentum, Player attackUser, Attack a){
		super(x, y, dirNum, momentum, attackUser, a);
		OnHitAction act = new OnHitAction(){
			public void f(){
				explode();
			}
		};
		getActionRegister().addOnHit(act);
	}
	public void explode(){
		ArrayList<AOEProjectile> aoe = new ArrayList<>();
		if(getAttack().getStatValue("AOE") != 0){
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
				getParent().insertChild(p);
			}
		}
	}
	public void update(){
		super.update();
		if(getDistance() >= getAttack().getStatValue("Range") && !hasAlreadyTerminated()){
			explode();
			terminate();
		}
	}
}
