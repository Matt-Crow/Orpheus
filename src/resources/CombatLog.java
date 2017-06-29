package resources;

import static java.lang.System.out;
import entities.Projectile;

public class CombatLog extends Op{
	public static void logProjectileData(Projectile p){
		add("-------");
		add("*Projectile:");
		add("Generated from " + p.getAttackName());
		add("by " + p.getUser().getName());
		add("hitting " + p.getHit().getName());
		add("after traveling " + p.getDistance() + " pixels.");
	}
	public static void displayLog(){
		out.println("<**COMBAT LOG**>");
		dp();
	}
}
