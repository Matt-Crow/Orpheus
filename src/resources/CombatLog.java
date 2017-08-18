package resources;

import static java.lang.System.out;
import java.util.ArrayList;
import entities.Projectile;

// extending Op did not work due to static stuff
public class CombatLog{
	private static ArrayList<String> log;
	
	private static void add(String msg){
		try {
			log.add(msg);
		} catch(NullPointerException e){
			log = new ArrayList<>();
			add(msg);
		}
	}
	public static void logProjectileData(Projectile p){
		add("-------");
		add("*Projectile:");
		add("Bearing " + p.getDirNum());
		add("Generated from " + p.getAttackName());
		add("by " + p.getUser().getName());
		add("hitting " + p.getHit().getName());
		add("after traveling " + p.getDistance() + " pixels.");
	}
	public static void displayLog(){
		out.println("<**COMBAT LOG**>");
		try{
			for(String msg : log){
				out.println(msg);
			}
		} catch(NullPointerException e){
			out.println("There is no data in the combat log");
		}
	}
}
