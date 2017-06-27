package entities;

import java.awt.Graphics;

import attacks.Attack;
import battle.Team;
import resources.CombatLog;

public class Projectile extends Entity{
	private Player user;
	private Attack registeredAttack;
	private int distanceTraveled;
	private Team firedBy;
	private String doNotHit;
	private String hit;
	private boolean isSeedProjectile;
	private boolean shouldTerminate;
	
	public Projectile(int x, int y, int dirNum, int momentum, Player attacker, Attack a){
		super(x, y, dirNum, momentum);
		setMoving(true);
		distanceTraveled = 0;
		user = attacker;
		firedBy = attacker.getTeam();
		attacker.getTeam().registerProjectile(this);
		registeredAttack = a;
		shouldTerminate = false;
		doNotHit = "None";
		hit = "None";
		isSeedProjectile = true;
	}
	public Projectile(int x, int y, int dirNum, int momentum, Team attacker, Attack a, Player ChainedFrom){
		super(x, y, dirNum, momentum);
		setMoving(true);
		distanceTraveled = 0;
		firedBy = attacker;
		attacker.registerAOEProjectile(this);
		registeredAttack = a;
		shouldTerminate = false;
		doNotHit = ChainedFrom.getName();
		hit = "None";
		isSeedProjectile = false;
	}
	public String getAttackName(){
		return registeredAttack.getName();
	}
	public String getUserName(){
		return user.getName();
	}
	public Player getHit(){
		return Player.getPlayerByName(hit);
	}
	public boolean checkIfWithin(int leftX, int rightX, int topY, int bottomY){
		return getX() >= leftX && getX() <= rightX && getY() >= topY && getY() <= bottomY;
	}
	public void checkForCollisionsWith(Player p){
		if(checkIfWithin(p.getX() - 50, p.getX() + 50, p.getY() - 50, p.getY() + 50)){
			if(p.getName() == doNotHit){
				return;
			}
			hit = p.getName();
			// add damage calc here
			terminate();
		}
	}
	public void terminate(){
		shouldTerminate = true;
		if(registeredAttack.getStatValue("AOE") != 0 && isSeedProjectile){
			for(int d = 0; d <= 7; d++){
				new Projectile(getX(), getY(), d, 5, firedBy, registeredAttack, Player.getPlayerByName(hit));
			}
		}
		CombatLog.logProjectileData(this);
	}
	public boolean getShouldTerminate(){
		return shouldTerminate;
	}
	public void update(){
		super.update();
		distanceTraveled += getMomentum();
		if(isSeedProjectile){
			if(distanceTraveled >= registeredAttack.getStatValue("Range")){
				terminate();
			}
		} else {
			if(distanceTraveled >= registeredAttack.getStatValue("AOE")){
				terminate();
			}
		}
	}
	public void draw(Graphics g){
		g.setColor(firedBy.color);
		g.fillOval(getX() - 25, getY() - 25, 50, 50);
	}
}
