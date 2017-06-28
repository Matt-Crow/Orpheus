package resources;

import static java.lang.System.out;
import java.util.ArrayList;
import entities.Projectile;

// extends Op
public class CombatLog{
	private static ArrayList<String> messages;
	
	public static void reset(){
		messages = new ArrayList<String>();
	}
	public static void add(String msg){
		try {
			messages.add(msg);
		} catch(NullPointerException e){
			reset();
			add(msg);
		}
	}
	public static void logProjectileData(Projectile p){
		add("-------");
		add("*Projectile:");
		add("Generated from " + p.getAttackName());
		add("by " + p.getUser().getName());
		add("hitting " + p.getHit().getName());
	}
	public static void displayLog(){
		out.println("<**COMBAT LOG**>");
		for(String message : messages){
			out.println(message);
		}
		reset();
	}
}
