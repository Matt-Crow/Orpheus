package world.entities;

import util.Settings;
import java.awt.Color;
import java.awt.Graphics;
import world.WorldContent;

public class Particle extends AbstractEntity{
	private final Color color;
	private final int lifeSpan;
	private int age;
	
	public Particle(WorldContent inWorld, int momentum, Color c){
		super(inWorld);
        setMaxSpeed(momentum);
		color = c;
		setRadius(5);
		lifeSpan = 15;
	}
    
    @Override
    public void init() {
        super.init();
        setIsMoving(true);
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
        super.update();
		age++;
		if(age >= lifeSpan){
			terminate();
		}
    }
}