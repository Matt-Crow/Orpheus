package actives;
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
import statuses.StatusTable;
import resources.Random;

public abstract class AbstractActive extends AbstractUpgradable{
	/**
	 * Actives are abilities that the user triggers
	 */
	private ActiveStatBaseValues bases; // 1-5 values used to generate this active
	private ParticleType particleType;
	private ArrayList<Color> particleColors;
	private ActiveType type; // used for upcasting
	
	private static ArrayList<AbstractActive> allActives = new ArrayList<>();
	
	public AbstractActive(ActiveType t, String n, int energyCost, int cooldown, int range, int speed, int aoe, int dmg){
		super(n);
		type = t;
		
		bases = new ActiveStatBaseValues(energyCost, cooldown, range, speed, aoe, dmg);
		
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
		
		particleType = ParticleType.NONE;
		particleColors = new ArrayList<>();
		particleColors.add(CustomColors.black);
	}
	public AbstractActive copy(){
		// used to allow override
		// DO NOT INVOKE THIS
		return this;
	}
	
	// static methods
	public static void addActive(AbstractActive a){
		allActives.add(a);
	}
	public static void addActives(AbstractActive[] as){
		for(AbstractActive a : as){
			addActive(a);
		}
	}
	public static AbstractActive getActiveByName(String n){
		AbstractActive ret = allActives.get(0);
		boolean found = false;
		for(int i = 0; i < allActives.size() && !found; i++){
			if(allActives.get(i).getName().equals(n)){
				ret = allActives.get(i);
				found = true;
			}
		}
		if(!found){
			throw new NullPointerException("No active was found with name " + n);
		}
		return ret;
	}
	public static AbstractActive[] getAll(){
		AbstractActive[] ret = new AbstractActive[allActives.size()];
		for(int i = 0; i < allActives.size(); i++){
			ret[i] = allActives.get(i).copy();
		}
		return ret;
	}
	
	public OnHitKey getStatusInfliction(){
		StatusTable inf = getInflict();
		OnHitKey a = new OnHitKey(){
			public void trip(OnHitTrip t){
				Player target = (Player)t.getHit();
				for(int i = 0; i < inf.getSize(); i++){
					if(Random.chance(inf.getChanceAt(i))){
						target.inflict(inf.getNameAt(i), inf.getIntensityAt(i), inf.getDurationAt(i));
					}
				}
			}
		};
		return a;
	}
	
	// particle methods
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
	public Color[] getColors(){
		Color[] ret = new Color[particleColors.size()];
		for(int i = 0; i < particleColors.size(); i++){
			ret[i] = particleColors.get(i);
		}
		return ret;
	}
	
	// misc
	public ActiveType getType(){
		return type;
	}
	public ActiveStatBaseValues getBases(){
		return bases;
	}
	
	// in battle methods
	public boolean canUse(){
		return getRegisteredTo().getEnergyLog().getEnergy() >= getStat("Energy Cost").get() && !onCooldown();
	}
	public void consumeEnergy(){
		getRegisteredTo().getEnergyLog().loseEnergy((int) getStatValue("Energy Cost"));
		setToCooldown();
	}
	public void use(){
		consumeEnergy();
		
		if(getStatValue("Damage") != 0){
			spawnProjectile();
		}
	}
	
	// spawning
	public void spawnProjectile(int facingDegrees){
		SeedProjectile registeredProjectile = new SeedProjectile(getRegisteredTo().getX(), getRegisteredTo().getY(), facingDegrees, (int) getStatValue("Speed"), getRegisteredTo(), this);
		registeredProjectile.getActionRegister().addOnHit(getStatusInfliction());
	}
	public void spawnProjectile(){
		spawnProjectile(getRegisteredTo().getDir().getDegrees());
	}
	public void spawnArc(int arcDegrees, int numProj){
		int spacing = arcDegrees / numProj;
		int start = getRegisteredTo().getDir().getDegrees() - arcDegrees / 2;
		
		for(int i = 0; i < numProj; i++){
			int angle = start + spacing * i;
			if(angle != getRegisteredTo().getDir().getDegrees()){
				// avoids launching 2 proj in facing direction due to initial invocation of spawnProjectile
				spawnProjectile(angle);
			}
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
			g.drawString("On cooldown: " + Master.framesToSeconds(getCooldown()), x + 10, y + 20);
		}	
	}
}
