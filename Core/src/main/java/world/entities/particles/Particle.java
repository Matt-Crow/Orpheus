package world.entities.particles;

import util.Settings;
import java.awt.Color;
import java.awt.Graphics;
import world.entities.AbstractPrimitiveEntity;

public class Particle extends AbstractPrimitiveEntity {
	private final Color color;
	private final int lifeSpan;
	
	public Particle(int momentum, Color c){
		super();
        setMaxSpeed(momentum);
		color = c;
		setRadius(5);
		lifeSpan = 15;
	}
    
    public int getLifeSpan(){
        return lifeSpan;
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
    public void init(){
        setIsMoving(true);
    }
    
    @Override
	public void update(){
        super.update();
    }
}