package world.entities;

import util.Settings;
import java.awt.Color;
import java.awt.Graphics;

public class Particle extends AbstractEntity{
	private final Color color;
	private final int lifeSpan;
	private int age;
	
	public Particle(int momentum, Color c){
		super();
        setSpeed(momentum);
		color = c;
		setRadius(5);
		lifeSpan = 15;
	}
    
    @Override
    public void init() {
        setMoving(true);
		age = 0;
    }
    
    @Override
	public void draw(Graphics g){
        if(!Settings.DISABLEPARTICLES){
            int r = getRadius();
            g.setColor(color);
            g.fillRect(getX() - r, getY() - r, r * 2, r * 2);
        }
	}
    
    @Override
	public void update(){
		age++;
		if(age >= lifeSpan){
			terminate();
		}
    }
}