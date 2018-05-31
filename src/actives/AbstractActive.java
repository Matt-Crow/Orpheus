package actives;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
import resources.Op;
import resources.Random;

public abstract class AbstractActive extends AbstractUpgradable{
	/**
	 * Actives are abilities that the user triggers
	 */
	private ParticleType particleType;
	private ArrayList<Color> particleColors;
	private ActiveType type; // used for upcasting
	
	private static HashMap<String, AbstractActive> allActives = new HashMap<>();
	
	public AbstractActive(ActiveType t, String n, int energyCost, int cooldown, int range, int speed, int aoe, int dmg){
		super(n);
		type = t;
		
		setStat(ActiveStat.COST, energyCost);
		setStat(ActiveStat.COOLDOWN, cooldown);
		setStat(ActiveStat.RANGE, range);
		setStat(ActiveStat.SPEED, speed);
		setStat(ActiveStat.AOE, aoe);
		setStat(ActiveStat.DAMAGE, dmg);
		
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
		allActives.put(a.getName().toUpperCase(), a);
	}
	public static void addActives(AbstractActive[] as){
		for(AbstractActive a : as){
			addActive(a);
		}
	}
	public static AbstractActive getActiveByName(String n){
		AbstractActive ret = allActives.getOrDefault(n, allActives.get("SLASH"));
		try{
			ret = allActives.get(n.toUpperCase());
		} catch(NullPointerException e){
			Op.add("No active was found with name " + n);
			Op.dp();
			e.printStackTrace();
		}
		return ret;
	}
	public static AbstractActive[] getAll(){
		AbstractActive[] ret = new AbstractActive[allActives.size()];
		Set<String> keys = allActives.keySet();
		int i = 0;
		for(String key : keys){
			ret[i] = allActives.get(key).copy();
			i++;
		}
		return ret;
	}
	
	public void setStat(ActiveStat n, int value){
		// 1-5 stat system
		switch(n){
		case COST:
			// 5-25 to 10-50 cost
			addStat(new Stat("Cost", value * 5, 2));
			setBase("Cost", value);
			break;
		case COOLDOWN:
			// 1-5 seconds
			// healing could be a problem
			addStat(new Stat("Cooldown", Master.seconds(value)));
			setBase("Cooldown", value);
			break;
		case RANGE:
			// 1-15 units of range. Increases exponentially
			int units = 0;
			for(int i = 0; i <= value; i++){
				units += i;
			}
			addStat(new Stat("Range", units * 100));
			setBase("Range", value);
			break;
		case SPEED:
			// 1-5 units per second
			addStat(new Stat("Speed", 100 * value / Master.FPS));
			setBase("Speed", value);
			break;
		case AOE:
			// 1-5 units (or 0)
			addStat(new Stat("AOE", value * 100));
			setBase("AOE", value);
			break;
		case DAMAGE:
			// 50-250 to 250-500 damage (will need to balance later?)
			addStat(new Stat("Damage", value * 50, 2));
			setBase("Damage", value);
			break;
		}
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
	
	// in battle methods
	public boolean canUse(){
		return getRegisteredTo().getEnergyLog().getEnergy() >= getStat("Cost").get() && !onCooldown();
	}
	public void consumeEnergy(){
		getRegisteredTo().getEnergyLog().loseEnergy((int) getStatValue("Cost"));
		setToCooldown();
	}
	public void use(){
		consumeEnergy();
		
		if(type != ActiveType.BOOST){
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