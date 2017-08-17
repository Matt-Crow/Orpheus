package attacks;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import entities.*;
import upgradables.Stat;
import statuses.Status;
import resources.OnHitAction;
import resources.Random;
import resources.Op;

public class Attack {
	private static ArrayList<Attack> attackList = new ArrayList<>();
	private String name;
	private ArrayList<Stat> stats;
	private ArrayList<Status> inflictOnHit;
	private ArrayList<Integer> inflictChance;
	private int cooldown;
	private Projectile registeredProjectile;
	private String type;
	
	public Attack(String n, int energyCost, int cooldown, int range, int speed, int aoe, int areaScale, int distanceScale, int dmg){
		name = n;
		stats = new ArrayList<>();
		stats.add(new Stat("Energy Cost", energyCost, 2));
		stats.add(new Stat("Cooldown", cooldown));
		stats.add(new Stat("Range", range));
		stats.add(new Stat("Speed", speed));
		stats.add(new Stat("AOE", aoe));
		stats.add(new Stat("Area Scale", areaScale));
		stats.add(new Stat("Distance Scale", distanceScale));
		stats.add(new Stat("Damage", dmg));
		
		inflictOnHit = new ArrayList<>();
		inflictChance = new ArrayList<>();
		
		attackList.add(this);
	}
	public static Attack getAttackByName(String name){
		for(Attack a : attackList){
			if(a.getName() == name){
				return a;
			}
		}
		return new Slash();
	}
	public void setType(String t){
		type = t;
	}
	public String getType(){
		return type;
	}
	public String getName(){
		return name;
	}
	public Stat getStat(String n){
		for(Stat stat : stats){
			if(stat.name == n){
				return stat;
			}
		}
		Op.add("The stat by the name of " + n + " is not found for Attack " + name);
		Op.dp();
		return new Stat("STATNOTFOUND", 0);
	}
	public double getStatValue(String n){
		return getStat(n).get();
	}
	
	public void addStatus(Status s, int chance){
		inflictOnHit.add(s);
		inflictChance.add(chance);
	}
	
	public Projectile getRegisteredProjectile(){
		return registeredProjectile;
	}
	
	public OnHitAction getStatusInfliction(){
		OnHitAction a = new OnHitAction(){
			public void f(){
				for(int i = 0; i < inflictOnHit.size(); i++){
					if(Random.chance(inflictChance.get(i))){
						getHit().inflict(inflictOnHit.get(i));
					}
				}
			}
		};
		return a;
	}
	
	public boolean onCooldown(){
		return cooldown > 0;
	}
	public boolean canUse(Player user){
		return user.getEnergyLog().getEnergy() >= getStat("Energy Cost").get() && !onCooldown();
	}
	public void use(Player user){
		user.getEnergyLog().loseEnergy((int) getStatValue("Energy Cost"));
		registeredProjectile = new SeedProjectile(user.getX(), user.getY(), user.getDirNum(), (int) getStatValue("Speed"), user, this);
		registeredProjectile.addOnHit(getStatusInfliction());
		if(registeredProjectile.getAttack().getStatValue("Range") == 0){
			registeredProjectile.terminate();
		}
		cooldown = (int) getStatValue("Cooldown");
		//displayData();
	}
	public void init(){
		cooldown = 0;
	}
	public void update(){
		cooldown -= 1;
		if (cooldown < 0){
			cooldown = 0;
		}
	}
	public void displayData(){
		for(Stat stat : stats){
			Op.add(stat.name + ": " + getStatValue(stat.name));
		}
		Op.dp();
	}
	public void drawStatusPane(Graphics g, int x, int y){
		if(!onCooldown()){
			g.setColor(Color.white);
			g.fillRect(x, y, 133, 50);
			g.setColor(Color.black);
			g.drawString(getName(), x + 10, y + 20);
		} else {
			g.setColor(Color.black);
			g.fillRect(x, y, 133, 50);
			g.setColor(Color.red);
			g.drawString("On cooldown: " + cooldown, x + 10, y + 20);
		}	
	}
}
