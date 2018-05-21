package entities;

import java.util.ArrayList;

import actions.OnHitTrip;
import actives.AbstractActive;
import actions.OnHitKey;

public class AOEProjectile extends Projectile{
	private ArrayList<AOEProjectile> brothers;
	public AOEProjectile(int x, int y, int degrees, int momentum, Player attackUser, AbstractActive a, Player chainedFrom){
		super(x, y, degrees, momentum, attackUser, a);
		setRange((int) a.getStatValue("aoe"));
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
