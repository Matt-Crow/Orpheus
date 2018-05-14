package entities;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import attacks.Attack;
import attacks.MeleeAttack;
import initializers.Master;
import resources.CombatLog;
import resources.Op;
import resources.Random;

public class Projectile extends Entity{
	private Player user;
	private Attack registeredAttack;
	private int distanceTraveled;
	private int range;
	private ArrayList<Player> doNotHit;
	private Player hit;
	
	public Projectile(int x, int y, int dirNum, int momentum, Player attackUser, Attack a){
		super(x, y, dirNum, momentum);
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
		
		Master.getCurrentBattle().getHost().getChunkContaining(x, y).register(this);
	}
	
	//node head manager
	public Projectile(){
		super(-1, -1, 0, 0);
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
	public Attack getAttack(){
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
	
	public boolean checkForCollisions(Entity e){
		boolean ret = false;
		if(super.checkForCollisions(e)){
			if(doNotHit.contains(e)){
				ret = true;
				if(e instanceof Player){
					hit((Player) e);
				}
			}
		}
		return ret;
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
					insertChild(new Particle(getX(), getY(), 360 * i / Master.TICKSTOROTATE, 5, rbu));
				}
				break;
			case SHEAR:
				Color rs = cs.get(Random.choose(0, cs.size() - 1));
				insertChild(new Particle(getX(), getY(), getDir().getDegrees() - 45, 5, rs));
				rs = cs.get(Random.choose(0, cs.size() - 1));
				insertChild(new Particle(getX(), getY(), getDir().getDegrees() + 45, 5, rs));
				break;
			case BEAM:
				Color rbe = cs.get(Random.choose(0, cs.size() - 1));
				insertChild(new Particle(getX(), getY(), getDir().getDegrees() - 180, 5, rbe));
				break;
			case BLADE:
				Color rbl = cs.get(Random.choose(0, cs.size() - 1));
				insertChild(new Particle(getX(), getY(), getDir().getDegrees(), 0, rbl));
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
