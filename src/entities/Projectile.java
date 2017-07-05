package entities;

import java.awt.Graphics;

import attacks.Attack;
import resources.CombatLog;

public class Projectile extends Entity{
	private Player user;
	private Attack registeredAttack;
	private int distanceTraveled;
	private Player doNotHit;
	private Player hit;
	private boolean shouldTerminate;
	private boolean terminated;
	
	public Projectile(int x, int y, int dirNum, int momentum, Player attackUser, Attack a){
		super(x, y, dirNum, momentum);
		setMoving(true);
		distanceTraveled = 0;
		user = attackUser;
		registeredAttack = a;
		shouldTerminate = false;
		terminated = false;
		doNotHit = new Player("Void");
		hit = new Player("Void");
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
	public void avoid(Player p){
		doNotHit = p;
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
			user.tripOnHit(p);
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
	}
	public void draw(Graphics g){
		g.setColor(user.getTeam().getColor());
		g.fillOval(getX() - 25, getY() - 25, 50, 50);
	}
}
