package entities;

import java.awt.Graphics;
import java.util.ArrayList;

import graphics.CustomColors;
import actives.AbstractActive;
import actives.MeleeActive;
import initializers.Master;
import resources.CombatLog;
import resources.Op;
import resources.Random;

public class Projectile extends Entity{
	private Player user;
	private AbstractActive registeredAttack;
	private int distanceTraveled;
	private int range;
	private Player hit;
	private ArrayList<Particle> particles;
	
	private int id; //used to prevent double hitting. May not be unique to a single projectile. See AbstractActive for more info
	
	public Projectile(int useId, int x, int y, int degrees, int momentum, Player attackUser, AbstractActive a){
		super(momentum);
		super.init(x, y, degrees);
		id = useId;
		distanceTraveled = 0;
		user = attackUser;
		setTeam(user.getTeam());
		registeredAttack = a;
		range = (int) a.getStatValue("Range");
		
		setMoving(true);
		hit = new Player("Void");
		particles = new ArrayList<>();
		setType(EntityType.PROJECTILE);
	}
	public int getUseId(){
		return id;
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
	public AbstractActive getAttack(){
		return registeredAttack;
	}
	
	public void hit(Player p){
		hit = p;
		p.logDamage((int) (registeredAttack.getStatValue("Damage") * user.getStatValue("damage dealt modifier") * p.getStatValue("damage taken modifier")));
		p.setLastHitById(getUseId());
		if(registeredAttack instanceof MeleeActive){
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
			ret = true;
			if(e.getType() == EntityType.PLAYER){
				hit((Player) e);
			}
		}
		return ret;
	}
	
	public void spawnParticle(int degrees, int m, CustomColors c){
		Particle p = new Particle(m, c);
		p.init(getX(), getY(), degrees);
		p.setTeam(this.getTeam());
		particles.add(p);
	}
	
	public void update(){
		super.update();
		
		for(Player p : getTeam().getEnemy().getMembersRem()){
			if(p.getLastHitById() != id){
				checkForCollisions(p);
			}
		}
		
		distanceTraveled += getMomentum();
		
		// need to change range based on projectile type: attack range for seed, aoe for aoeprojectile
		if(distanceTraveled >= range && !getShouldTerminate()){
			terminate();
		}
		
		CustomColors[] cs = user.getCharacterClass().getColors();
		
		if(!Master.DISABLEPARTICLES){
			switch(registeredAttack.getParticleType()){
			case BURST:
				for(int i = 0; i < Master.TICKSTOROTATE; i++){
					CustomColors rbu = cs[Random.choose(0, cs.length - 1)];
					spawnParticle(360 * i / Master.TICKSTOROTATE, 5, rbu);
				}
				break;
			case SHEAR:
				CustomColors rs = cs[Random.choose(0, cs.length - 1)];
				spawnParticle(getDir().getDegrees() - 45, 5, rs);
				rs = cs[Random.choose(0, cs.length - 1)];
				spawnParticle(getDir().getDegrees() + 45, 5, rs);
				break;
			case BEAM:
				CustomColors rbe = cs[Random.choose(0, cs.length - 1)];
				spawnParticle(getDir().getDegrees() - 180, 5, rbe);
				break;
			case BLADE:
				CustomColors rbl = cs[Random.choose(0, cs.length - 1)];
				spawnParticle(getDir().getDegrees(), 0, rbl);
				break;
			default:
				Op.add("The particle type of " + registeredAttack.getParticleType());
				Op.add("is not found for Projectile.java");
				Op.dp();
			}
			particles.stream().forEach(p -> p.update());
			ArrayList<Particle> newPart = new ArrayList<>();
			particles.stream().filter(p -> !p.getShouldTerminate()).forEach(p -> newPart.add(p));
			particles = newPart;
		}
	}
	public void draw(Graphics g){
		if(registeredAttack.getParticleType() == ParticleType.NONE || Master.DISABLEPARTICLES){
			g.setColor(user.getTeam().getColor());
			g.fillOval(getX() - 25, getY() - 25, 50, 50);
		} 
		if(!Master.DISABLEPARTICLES){
			particles.stream().forEach(p -> p.draw(g));
		}
	}
}
