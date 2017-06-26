package entities;

import java.awt.Graphics;

import attacks.Attack;
import battle.Team;
import resources.Op;

public class Projectile extends Entity{
	private Attack registeredAttack;
	private int distanceTraveled;
	private Team firedBy;
	private Player doNotHit;
	private Player hit;
	private boolean isSeedProjectile;
	private boolean shouldTerminate;
	
	public Projectile(int x, int y, int dirNum, int momentum, Team attacker, Attack a){
		super(x, y, dirNum, momentum);
		setMoving(true);
		distanceTraveled = 0;
		firedBy = attacker;
		attacker.registerProjectile(this);
		registeredAttack = a;
		shouldTerminate = false;
		doNotHit = new Player("Nerdwill");
		hit = new Player("Neville");
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
		doNotHit = ChainedFrom;
		hit = new Player("Neville");
		isSeedProjectile = false;
	}
	public boolean checkIfWithin(int leftX, int rightX, int topY, int bottomY){
		return getX() >= leftX && getX() <= rightX && getY() >= topY && getY() <= bottomY;
	}
	public void checkForCollisionsWith(Player p){
		if(checkIfWithin(p.getX() - 50, p.getX() + 50, p.getY() - 50, p.getY() + 50)){
			if(p == doNotHit){
				return;
			}
			hit = p;
			// add damage calc here
			terminate();
		}
	}
	public void terminate(){
		shouldTerminate = true;
		if(registeredAttack.getStatValue("AOE") != 0 && isSeedProjectile){
			for(int d = 0; d <= 7; d++){
				new Projectile(getX(), getY(), d, 5, firedBy, registeredAttack, hit);
			}
		}
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
