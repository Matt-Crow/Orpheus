package entities;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;

public class Particle extends Entity{
	private static ArrayList<Particle> particles = new ArrayList<>();
	private Color color;
	private int size;
	private int lifeSpan;
	private int age;
	private boolean shouldTerminate;
	
	public Particle(int x, int y, int dirNum, int momentum, Color c){
		super(x, y, dirNum, momentum);
		setMoving(true);
		color = c;
		size = 10;
		lifeSpan = 15;
		age = 0;
		shouldTerminate = false;
		particles.add(this);
	}
	public static void reset(){
		particles = new ArrayList<>();
	}
	public boolean getShouldTerminate(){
		return shouldTerminate;
	}
	public void draw(Graphics g){
		g.setColor(color);
		g.fillRect(getX(), getY(), size, size);
	}
	public static void drawAll(Graphics g){
		for(Particle p : particles){
			p.draw(g);
		}
	}
	public void update(){
		super.update();
		age++;
		shouldTerminate = age >= lifeSpan;
	}
	public static void updateAll(){
		ArrayList<Particle> newArray = new ArrayList<>();
		for(Particle p : particles){
			p.update();
			if(!p.getShouldTerminate()){
				newArray.add(p);
			}
		}
		particles = newArray;
	}
}
