package attacks;
import java.util.ArrayList;

import actions.OnHitTrip;
import actions.OnHitKey;

import java.awt.Color;
import java.awt.Graphics;
import entities.*;
import graphics.CustomColors;
import initializers.Master;
import upgradables.AbstractUpgradable;
import upgradables.Stat;
import statuses.Status;
import resources.Random;

public class Attack extends AbstractUpgradable{
	private ArrayList<Status> inflictOnHit;
	private ArrayList<Integer> inflictChance;
	
	
	// find some way so that this doesn't include terminated projectiles
	// maybe update them from here?
	private ArrayList<Projectile> lastUseChildren;
	
	private boolean projectilesTrack;
	
	private ParticleType particleType;
	private ArrayList<Color> particleColors;
	
	public Attack(String n, int energyCost, int cooldown, int range, int speed, int aoe, int dmg){
		super(n);
		// 1-5 stat system
		
		// 5-25 to 10-50 cost
		addStat(new Stat("Energy Cost", energyCost * 5, 2));
		
		// 1-5 seconds
		// healing could be a problem
		addStat(new Stat("Cooldown", Master.seconds(cooldown)));
		
		// 1-15 units of range. Increases exponentially
		int units = 0;
		for(int i = 0; i <= range; i++){
			units += i;
		}
		addStat(new Stat("Range", units * 100));
		
		// 1-5 units per second
		addStat(new Stat("Speed", 100 * speed / Master.FPS));
		
		// 1-5 units (or 0)
		addStat(new Stat("AOE", aoe * 100));
		
		// 50-250 to 250-500 damage (will need to balance later?)
		addStat(new Stat("Damage", dmg * 50, 2));
		
		inflictOnHit = new ArrayList<>();
		inflictChance = new ArrayList<>();
		
		projectilesTrack = false;
		
		particleType = ParticleType.NONE;
		particleColors = new ArrayList<>();
		particleColors.add(CustomColors.black);
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
	
	
	
	
	public OnHitKey getStatusInfliction(){
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				Player target = (Player)t.getHit();
				for(int i = 0; i < inflictOnHit.size(); i++){
					if(Random.chance(inflictChance.get(i))){
						target.inflict(inflictOnHit.get(i));
					}
				}
			}
		};
		return a;
	}
	
	public boolean canUse(){
		return getRegisteredTo().getEnergyLog().getEnergy() >= getStat("Energy Cost").get() && !onCooldown();
	}
	
	public void consumeEnergy(){
		getRegisteredTo().getEnergyLog().loseEnergy((int) getStatValue("Energy Cost"));
		setToCooldown();
	}
	
	public void use(){
		lastUseChildren = new ArrayList<>();
		consumeEnergy();
		
		spawnProjectile();
	}
	
	public void spawnProjectile(int facingDegrees){
		SeedProjectile registeredProjectile = new SeedProjectile(getRegisteredTo().getX(), getRegisteredTo().getY(), facingDegrees, (int) getStatValue("Speed"), getRegisteredTo(), this);
		registeredProjectile.getActionRegister().addOnHit(getStatusInfliction());
		
		lastUseChildren.add(registeredProjectile);
	}
	public void spawnProjectile(){
		spawnProjectile(getRegisteredTo().getDir().getDegrees());
	}
	public void spawnArc(int arcDegrees, int numProj){
		int spacing = arcDegrees / numProj;
		int start = getRegisteredTo().getDir().getDegrees() - arcDegrees / 2;
		
		for(int i = 0; i < numProj; i++){
			int angle = start + spacing * i;
			spawnProjectile(angle);
		}
	}
	
	
	public void init(){
		super.init();
		lastUseChildren = new ArrayList<>();
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
			g.drawString("On cooldown: " + Master.framesToSeconds(getCooldown()), x + 10, y + 20);
		}	
	}
}
