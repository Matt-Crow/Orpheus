package entities;

import java.util.ArrayList;
import attacks.Attack;
import resources.OnHitAction;
import resources.Direction;

public class AOEProjectile extends Projectile{
	private ArrayList<AOEProjectile> brothers;
	public AOEProjectile(int x, int y, Direction dirNum, int momentum, Player attackUser, Attack a, Player chainedFrom){
		super(x, y, dirNum, momentum, attackUser, a);
		avoid(chainedFrom);
		brothers = new ArrayList<>();
		OnHitAction act = new OnHitAction(){
			public void f(){
				for(AOEProjectile p : brothers){
					p.avoid(getHit());
				}
			}
		};
		this.addOnHit(act);
	}
	public void addBrother(AOEProjectile p){
		brothers.add(p);
	}
	public void update(){
		super.update();
		if(getDistance() >= getAttack().getStatValue("AOE")){
			terminate();
		}
	}
}
