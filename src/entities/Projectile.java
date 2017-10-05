package entities;

import java.awt.Graphics;
import java.util.ArrayList;
import attacks.Attack;
import initializers.Master;
import resources.CombatLog;
import resources.CustomColors;
import resources.Op;

public class Projectile extends Entity{
	private Player user;
	private Attack registeredAttack;
	private int distanceTraveled;
	private ArrayList<Player> doNotHit;
	private Player hit;
	private boolean shouldTerminate;
	private boolean terminated;
	private ParticleType particleType;
	
	public Projectile(int x, int y, int dirNum, int momentum, Player attackUser, Attack a){
		super(x, y, dirNum, momentum);
		setMoving(true);
		distanceTraveled = 0;
		user = attackUser;
		registeredAttack = a;
		shouldTerminate = false;
		terminated = false;
		particleType = ParticleType.NONE;
		
		doNotHit = new ArrayList<Player>();
		hit = new Player("Void");
		
		user.getTeam().registerProjectile(this);
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
	public boolean getShouldTerminate(){
		return shouldTerminate;
	}
	public boolean hasAlreadyTerminated(){
		return terminated;
	}
	public void setParticleType(ParticleType t){
		particleType = t;
	}
	public ParticleType getParticleType(){
		return particleType;
	}
	public void avoid(Player p){
		doNotHit.add(p);
	}
	public boolean checkIfWithin(int leftX, int rightX, int topY, int bottomY){
		return getX() >= leftX && getX() <= rightX && getY() >= topY && getY() <= bottomY;
	}
	public void checkForCollisionsWith(Player p){
		if(checkIfWithin(p.getX() - 50, p.getX() + 50, p.getY() - 50, p.getY() + 50)){
			if(doNotHit.contains(p)){
				return;
			}
			hit = p;
			// add damage calc here
			p.logDamage((int) registeredAttack.getStatValue("Damage"));
			
			if(registeredAttack.getType() == "melee"){
				user.tripOnMeleeHit(p);
				user.getEnergyLog().gainEnergy((int) user.getStatValue("EPH"));
				p.tripOnBeMeleeHit(user);
				p.getEnergyLog().gainEnergy((int) p.getStatValue("EPHR"));
			} else {
				user.tripOnHit(p);
				p.tripOnBeHit(user);
			}
			tripOnHit(p);
			CombatLog.logProjectileData(this);
			terminate();
		}
	}
	public void terminate(){
		shouldTerminate = true;
		terminated = true;
	}
	public void update(){
		super.update();
		distanceTraveled += getMomentum();
		switch(particleType){
		case BURST:
			// i = 1 to avoid divide by 0
			for(int i = 1; i <= Master.TICKSTOROTATE; i++){
				new Particle(getX(), getY(), 360 / i * Master.TICKSTOROTATE, 5, CustomColors.black);
			}
			break;
		default:
			Op.add("Particle type of " + particleType + " not found in Projectile.java");
			Op.dp();
		}
	}
	public void draw(Graphics g){
		g.setColor(user.getTeam().getColor());
		g.fillOval(getX() - 25, getY() - 25, 50, 50);
	}
}
