package entities;

import java.util.ArrayList;

import actions.OnHitTrip;
import actions.OnHitKey;
import attacks.Attack;

public class AOEProjectile extends Projectile{
	private ArrayList<AOEProjectile> brothers;
	public AOEProjectile(int x, int y, int dirNum, int momentum, Player attackUser, Attack a, Player chainedFrom){
		super(x, y, dirNum, momentum, attackUser, a);
		avoid(chainedFrom);
		brothers = new ArrayList<>();
		OnHitKey act = new OnHitKey(){
			public void trip(OnHitTrip t){
				for(AOEProjectile p : brothers){
					p.avoid((Player)t.getHit());
				}
			}
		};
		this.getActionRegister().addOnHit(act);
	}
	public void addBrother(AOEProjectile p){
		brothers.add(p);
	}
	public void update(){
		super.update();
		
		if(getDistance() >= getAttack().getStatValue("AOE") && !getShouldTerminate()){
			terminate();
		}
	}
}
