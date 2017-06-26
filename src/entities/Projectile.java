package entities;

import java.awt.Graphics;

import attacks.Attack;
import battle.Team;
import resources.Op;

public class Projectile extends Entity{
	private Attack registeredAttack;
	private int distanceTraveled;
	private Team firedBy;
	private boolean shouldTerminate;
	
	public Projectile(int x, int y, int dirNum, int momentum, Team attacker, Attack a){
		super(x, y, dirNum, momentum);
		setMoving(true);
		distanceTraveled = 0;
		firedBy = attacker;
		attacker.registerProjectile(this);
		registeredAttack = a;
		shouldTerminate = false;
	}
	public void checkForCollisionsWith(Player p){
		if(checkIfWithin(p.getX() - 50, p.getX() + 50, p.getY() - 50, p.getY() + 50)){
			terminate();
		}
	}
	public boolean checkIfWithin(int leftX, int rightX, int topY, int bottomY){
		return getX() >= leftX && getX() <= rightX && getY() >= topY && getY() <= bottomY;
	}
	public void terminate(){
		shouldTerminate = true;
		if(registeredAttack.getStatValue("AOE") != 0){
			for(int d = 0; d <= 7; d++){
				// make a special AOEProjectile class that CANNOT CHAIN. Hit cooldown so doesnt all hit one person
				new Projectile(getX(), getY(), d, 5, firedBy, registeredAttack);
			}
		}
	}
	public boolean getShouldTerminate(){
		return shouldTerminate;
	}
	public void update(){
		super.update();
		distanceTraveled += getMomentum();
		if(distanceTraveled >= registeredAttack.getStatValue("Range")){
			shouldTerminate = true;
		}
	}
	public void draw(Graphics g){
		g.setColor(firedBy.color);
		g.fillOval(getX() - 25, getY() - 25, 50, 50);
	}
}
