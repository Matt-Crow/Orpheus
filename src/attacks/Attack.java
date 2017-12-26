package attacks;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import entities.*;
import upgradables.Stat;
import statuses.Status;
import resources.CustomColors;
import resources.OnHitAction;
import resources.Random;
import resources.Op;

public class Attack {
	private String name;
	private ArrayList<Stat> stats;
	private ArrayList<Status> inflictOnHit;
	private ArrayList<Integer> inflictChance;
	private int cooldown;
	
	
	//find some way to get rid of this
	private Projectile registeredProjectile;
	
	
	private ParticleType particleType;
	private ArrayList<Color> particleColors;
	
	public Attack(String n, int energyCost, int cooldown, int range, int speed, int aoe, int dmg){
		// 1-5 stat system
		name = n;
		stats = new ArrayList<>();
		
		// 5-25 to 10-50 cost
		stats.add(new Stat("Energy Cost", energyCost * 5, 2));
		
		// 20-100 frames (see initializers.Master for second count)
		// healing could be a problem
		stats.add(new Stat("Cooldown", cooldown * 20));
		
		// 1-15 units of range. Increases exponentially
		int units = 0;
		for(int i = 0; i <= range; i++){
			units += i;
		}
		stats.add(new Stat("Range", units * 100));
		
		// 1-5 units per 20 frames
		stats.add(new Stat("Speed", speed * 5));
		
		// 1-5 units (or 0)
		stats.add(new Stat("AOE", aoe * 100));
		
		// 50-250 to 250-500 damage (will need to balance later?)
		stats.add(new Stat("Damage", dmg * 50, 2));
		
		inflictOnHit = new ArrayList<>();
		inflictChance = new ArrayList<>();
		
		particleType = ParticleType.NONE;
		particleColors = new ArrayList<>();
		particleColors.add(CustomColors.black);
	}
	public String getName(){
		return name;
	}
	public Stat getStat(String n){
		Stat ret = new Stat("STATNOTFOUND", 0);
		for(Stat stat : stats){
			if(stat.name == n){
				ret = stat;
			}
		}
		Op.add("The stat by the name of " + n + " is not found for Attack " + name);
		Op.dp();
		return ret;
	}
	public double getStatValue(String n){
		return getStat(n).get();
	}
	
	public void addStatus(Status s, int chance){
		inflictOnHit.add(s);
		inflictChance.add(chance);
	}
	public void setToCooldown(){
		cooldown = (int) getStatValue("Cooldown");
	}
	public void setRegisteredProjectile(Projectile p){
		registeredProjectile = p;
	}
	public void setParticleType(ParticleType t){
		particleType = t;
	}
	public ParticleType getParticleType(){
		return particleType;
	}
	public void setParticleColor(Color c){
		particleColors = new ArrayList<>();
		particleColors.add(c);
	}
	public void setColorBlend(Color[] cs){
		particleColors = new ArrayList<>();
		for(Color c : cs){
			particleColors.add(c);
		}
	}
	public ArrayList<Color> getColors(){
		return particleColors;
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
		registeredProjectile = new SeedProjectile(user.getX(), user.getY(), user.getDir().getDegrees(), (int) getStatValue("Speed"), user, this);
		registeredProjectile.getActionRegister().addOnHit(getStatusInfliction());
		if(registeredProjectile.getAttack().getStatValue("Range") == 0){
			registeredProjectile.terminate();
		}
		setToCooldown();
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
	public void drawStatusPane(Graphics g, int x, int y, int w, int h){
		if(!onCooldown()){
			g.setColor(Color.white);
			g.fillRect(x, y, w, h);
			g.setColor(Color.black);
			g.drawString(getName(), x + 10, y + 20);
		} else {
			g.setColor(Color.black);
			g.fillRect(x, y, w, h);
			g.setColor(Color.red);
			g.drawString("On cooldown: " + cooldown, x + 10, y + 20);
		}	
	}
}
