package entities;

import attacks.Attack;
import resources.Direction;
import initializers.Master;

public class SeedProjectile extends Projectile{
	public SeedProjectile(int x, int y, Direction dirNum, int momentum, Player attackUser, Attack a){
		super(x, y, dirNum, momentum, attackUser, a);
		attackUser.getTeam().registerProjectile(this);
	}
	public void terminate(){
		super.terminate();
		if(getAttack().getStatValue("AOE") != 0){
			for(int i = 0; i < Master.TICKSTOROTATE; i++){
				AOEProjectile p = new AOEProjectile(getX(), getY(), new Direction(360 * i / Master.TICKSTOROTATE), 5, getUser(), getAttack(), getHit());
				p.addOnHit(getAttack().getStatusInfliction());
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
