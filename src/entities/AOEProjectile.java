package entities;

import attacks.Attack;

public class AOEProjectile extends Projectile{
	public AOEProjectile(int x, int y, double dirNum, int momentum, Player attackUser, Attack a, Player chainedFrom){
		super(x, y, dirNum, momentum, attackUser, a);
		avoid(chainedFrom);
		attackUser.getTeam().registerProjectile(this);
	}
	public void update(){
		super.update();
		if(getDistance() >= getAttack().getStatValue("AOE")){
			terminate();
		}
	}
}
