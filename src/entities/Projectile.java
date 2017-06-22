package entities;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

import attacks.Attack;

public class Projectile extends Entity{
	private static ArrayList<Projectile> register;
	private Attack registeredAttack;
	private int distanceTraveled;
	
	public Projectile(int x, int y, int dirNum, int momentum, Attack a){
		super(x, y, dirNum, momentum);
		setMoving(true);
		distanceTraveled = 0;
		registeredAttack = a;
		register.add(this);
	}
	
	public static ArrayList<Projectile> getRegister(){
		return register;
	}
	public static void init(){
		register = new ArrayList<>();
	}
	public static void updateProjectiles(){
		ArrayList<Projectile> newList = new ArrayList<>();
		for(Projectile p : register){
			p.update();
			p.distanceTraveled += p.getMomentum();
			if(p.distanceTraveled < p.registeredAttack.getStatValue("Range")){
				newList.add(p);
			}
		}
		register = newList;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.gray);
		g.fillOval(getX() - 25, getY() - 25, 50, 50);
	}
}
