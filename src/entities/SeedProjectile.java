package entities;

import attacks.Attack;

public class SeedProjectile extends Projectile{
	public SeedProjectile(int x, int y, double dirNum, int momentum, Player attackUser, Attack a){
		super(x, y, dirNum, momentum, attackUser, a);
		attackUser.getTeam().registerProjectile(this);
	}
	public void terminate(){
		super.terminate();
		if(getAttack().getStatValue("AOE") != 0){
			for(double d = 0; d <= 2; d += 0.1){
				AOEProjectile p = new AOEProjectile(getX(), getY(), d, 5, getUser(), getAttack(), getHit());
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
