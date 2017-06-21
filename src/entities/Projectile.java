package entities;

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class Projectile extends Entity{
	private static ArrayList<Projectile> register;
	
	public Projectile(int x, int y, int dirNum, int momentum){
		super(x, y, dirNum, momentum);
		setMoving(true);
		register.add(this);
	}
	public static ArrayList<Projectile> getRegister(){
		return register;
	}
	public static void init(){
		register = new ArrayList<>();
	}
	public static void updateProjectiles(){
		for(Projectile p : register){
			p.update();
		}
	}
	public void draw(Graphics g){
		g.setColor(Color.gray);
		g.fillOval(getX() - 25, getY() - 25, 50, 50);
	}
}
