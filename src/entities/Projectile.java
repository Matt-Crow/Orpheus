package entities;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

import actives.Active;
import actives.MeleeAttack;
import initializers.Master;
import resources.CombatLog;
import resources.Op;
import resources.Random;

public class Projectile extends Entity{
	private Player user;
	private Active registeredAttack;
	private int distanceTraveled;
	private int range;
	private ArrayList<Player> doNotHit;
	private Player hit;
	
	public Projectile(int x, int y, int degrees, int momentum, Player attackUser, Active a){
		super(momentum);
		super.init(x, y, degrees);
		distanceTraveled = 0;
		user = attackUser;
		setTeam(user.getTeam());
		registeredAttack = a;
		range = (int) a.getStatValue("Range");
		if(a.getTracking()){
			getEntityAI().enable();
		}
		
		setMoving(true);
		doNotHit = new ArrayList<Player>();
		hit = new Player("Void");
		
		setType(EntityType.PROJECTILE);
	}
	
	public void setRange(int i){
		range = i;
	}
	
	public String getAttackName(){
		return registeredAttack.getName();
	}
	public Player getUser(){
		return user;
	}
	public Player getHit(){
		return hit;
	}
	public int getDistance(){
		return distanceTraveled;
	}
	public Active getAttack(){
		return registeredAttack;
	}
	
	public void avoid(Player p){
		doNotHit.add(p);
	}
	
	public void hit(Player p){
		hit = p;
		p.logDamage((int) (registeredAttack.getStatValue("Damage") * user.getStatValue("damage dealt modifier") * p.getStatValue("damage taken modifier")));
		if(registeredAttack instanceof MeleeAttack){
			user.getActionRegister().tripOnMeleeHit(p);
			user.getEnergyLog().gainEnergy(5);
			p.getActionRegister().tripOnBeMeleeHit(user);
		} else {
			user.getActionRegister().tripOnHit(p);
			p.getActionRegister().tripOnBeHit(user);
		}
		getActionRegister().tripOnHit(p);
		CombatLog.logProjectileData(this);
		terminate();
	}
	
	// TODO: add checking for collisions with particles, other stuff
	public boolean checkForCollisions(Entity e){
		boolean ret = false;
		if(super.checkForCollisions(e)){
			if(!doNotHit.contains(e)){
				ret = true;
				if(e.getType() == EntityType.PLAYER){
					hit((Player) e);
				}
			}
		}
		return ret;
	}
	
	public void spawnParticle(int degrees, int m, Color c){
		Particle p = new Particle(m, c);
		p.init(getX(), getY(), degrees);
		p.setTeam(this.getTeam());
	}
	
	public void update(){
		super.update();
		distanceTraveled += getMomentum();
		
		// need to change range based on projectile type: attack range for seed, aoe for aoeprojectile
		if(distanceTraveled >= range && !getShouldTerminate()){
			terminate();
		}
		
		ArrayList<Color> cs = registeredAttack.getColors();
		
		if(!Master.DISABLEPARTICLES){
			switch(registeredAttack.getParticleType()){
			case BURST:
				for(int i = 0; i < Master.TICKSTOROTATE; i++){
					Color rbu = cs.get(Random.choose(0, cs.size() - 1));
					spawnParticle(360 * i / Master.TICKSTOROTATE, 5, rbu);
				}
				break;
			case SHEAR:
				Color rs = cs.get(Random.choose(0, cs.size() - 1));
				spawnParticle(getDir().getDegrees() - 45, 5, rs);
				rs = cs.get(Random.choose(0, cs.size() - 1));
				spawnParticle(getDir().getDegrees() + 45, 5, rs);
				break;
			case BEAM:
				Color rbe = cs.get(Random.choose(0, cs.size() - 1));
				spawnParticle(getDir().getDegrees() - 180, 5, rbe);
				break;
			case BLADE:
				Color rbl = cs.get(Random.choose(0, cs.size() - 1));
				spawnParticle(getDir().getDegrees(), 0, rbl);
				break;
			default:
				Op.add("The particle type of " + registeredAttack.getParticleType());
				Op.add("is not found for Projectile.java");
				Op.dp();
			}
		}
	}
	public void draw(Graphics g){
		if(registeredAttack.getParticleType() == ParticleType.NONE || Master.DISABLEPARTICLES){
			g.setColor(user.getTeam().getColor());
			g.fillOval(getX() - 25, getY() - 25, 50, 50);
		}
	}
}
