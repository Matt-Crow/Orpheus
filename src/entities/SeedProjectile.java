package entities;

import java.util.ArrayList;
import attacks.Attack;
import resources.Direction;
import initializers.Master;

public class SeedProjectile extends Projectile{
	public SeedProjectile(int x, int y, Direction dirNum, int momentum, Player attackUser, Attack a){
		super(x, y, dirNum, momentum, attackUser, a);
	}
	public void terminate(){
		super.terminate();
		
		ArrayList<AOEProjectile> aoe = new ArrayList<>();
		if(getAttack().getStatValue("AOE") != 0){
			for(int i = 0; i < Master.TICKSTOROTATE; i++){
				aoe.add(new AOEProjectile(getX(), getY(), new Direction(360 * i / Master.TICKSTOROTATE), 5, getUser(), getAttack(), getHit()));
			}
			for(AOEProjectile p : aoe){
				p.addOnHit(getAttack().getStatusInfliction());
				for(AOEProjectile possibleBrother : aoe){
					if(p != possibleBrother){
						p.addBrother(possibleBrother);
					}
				}
			}
		}
	}
	public void update(){
		super.update();
		if(getDistance() >= getAttack().getStatValue("Range") && !hasAlreadyTerminated()){
			terminate();
		}
	}
}
