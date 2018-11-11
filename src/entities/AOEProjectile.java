package entities;

import actives.AbstractActive;
import actives.ActiveStatName;

//TODO: replace this with explosion later
public class AOEProjectile extends Projectile{
	public AOEProjectile(int id, int x, int y, int degrees, int momentum, Player attackUser, AbstractActive a){
		super(id, x, y, degrees, momentum, attackUser, a);
		setRange((int) a.getStatValue(ActiveStatName.AOE));
	}
}
