package attacks;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import entities.*;
import initializers.Master;
import upgradables.Stat;
import statuses.Status;
import resources.CustomColors;
import resources.OnHitAction;
import resources.Random;


//add user
public class Attack {
	private String name;
	private ArrayList<Stat> stats;
	private ArrayList<Status> inflictOnHit;
	private ArrayList<Integer> inflictChance;
	private int cooldown;
	private Player user;
	
	// find some way so that this doesn't include terminated projectiles
	// maybe update them from here?
	private ArrayList<Projectile> lastUseChildren;
	
	private Projectile head;
	
	private boolean projectilesTrack;
	
	private ParticleType particleType;
	private ArrayList<Color> particleColors;
	
	public Attack(String n, int energyCost, int cooldown, int range, int speed, int aoe, int dmg){
		// 1-5 stat system
		name = n;
		stats = new ArrayList<>();
		
		// 5-25 to 10-50 cost
		stats.add(new Stat("Energy Cost", energyCost * 5, 2));
		
		// 1-5 seconds
		// healing could be a problem
		stats.add(new Stat("Cooldown", Master.seconds(cooldown)));
		
		// 1-15 units of range. Increases exponentially
		int units = 0;
		for(int i = 0; i <= range; i++){
			units += i;
		}
		stats.add(new Stat("Range", units * 100));
		
		// 1-5 units per second
		stats.add(new Stat("Speed", 100 * speed / Master.FPS));
		
		// 1-5 units (or 0)
		stats.add(new Stat("AOE", aoe * 100));
		
		// 50-250 to 250-500 damage (will need to balance later?)
		stats.add(new Stat("Damage", dmg * 50, 2));
		
		inflictOnHit = new ArrayList<>();
		inflictChance = new ArrayList<>();
		
		projectilesTrack = false;
		
		particleType = ParticleType.NONE;
		particleColors = new ArrayList<>();
		particleColors.add(CustomColors.black);
	}
	public void setUser(Player p){
		user = p;
	}
	public Player getUser(){
		return user;
	}
	public String getName(){
		return name;
	}
	public Stat getStat(String n){
		Stat ret = new Stat("STATNOTFOUND", 0);
		for(Stat stat : stats){
			if(stat.getName() == n){
				ret = stat;
			}
		}
		if(ret.getName() == "STATNOTFOUND"){
			throw new NullPointerException();
		}
		return ret;
	}
	public double getStatValue(String n){
		return getStat(n).get();
	}
	public void enableTracking(){
		projectilesTrack = true;
	}
	public boolean getTracking(){
		return projectilesTrack;
	}
	
	public void addStatus(Status s, int chance){
		inflictOnHit.add(s);
		inflictChance.add(chance);
	}
	
	public void setToCooldown(){
		cooldown = (int) getStatValue("Cooldown");
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
	
	
	
	
	
	public ArrayList<Projectile> getLastUseProjectiles(){
		return lastUseChildren;
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
	public boolean canUse(){
		return user.getEnergyLog().getEnergy() >= getStat("Energy Cost").get() && !onCooldown();
	}
	
	public void consumeEnergy(){
		user.getEnergyLog().loseEnergy((int) getStatValue("Energy Cost"));
		setToCooldown();
	}
	
	public void use(){
		lastUseChildren = new ArrayList<>();
		consumeEnergy();
		spawnProjectile(); // remove?
	}
	
	
	
	
	public void spawnProjectile(int facingDegrees){
		SeedProjectile registeredProjectile = new SeedProjectile(user.getX(), user.getY(), facingDegrees, (int) getStatValue("Speed"), user, this);
		registeredProjectile.getActionRegister().addOnHit(getStatusInfliction());
		if(registeredProjectile.getAttack().getStatValue("Range") == 0){
			registeredProjectile.terminate();
		}
		head.insertChild(registeredProjectile);
		lastUseChildren.add(registeredProjectile);
	}
	public void spawnProjectile(){
		spawnProjectile(user.getDir().getDegrees());
	}
	
	public void spawnArc(int arcDegrees, int numProj){
		int spacing = arcDegrees / numProj;
		int start = user.getDir().getDegrees() - arcDegrees / 2;
		
		for(int i = 0; i < numProj; i++){
			int angle = start + spacing * i;
			spawnProjectile(angle);
		}
	}
	
	
	public void init(){
		cooldown = 0;
		lastUseChildren = new ArrayList<>();
		head = new Projectile(this);
	}
	
	
	public void update(){
		cooldown -= 1;
		if (cooldown < 0){
			cooldown = 0;
		}
		head.updateAllChildren();
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
	
	public void drawAllProjectiles(Graphics g){
		Projectile current = head;
		while(current.getHasChild()){
			current = (Projectile) current.getChild();
			current.draw(g);
		}
	}
}
