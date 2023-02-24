package world.entities.particles;

import java.awt.Color;
import orpheus.core.world.graph.Graphable;
import world.entities.AbstractPrimitiveEntity;

public class Particle extends AbstractPrimitiveEntity implements Graphable {
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
    public void init(){
        setIsMoving(true);
    }
    
    @Override
	public void update(){
        super.update();
    }

    @Override
    public orpheus.core.world.graph.Particle toGraph() {
        return new orpheus.core.world.graph.Particle(getX(), getY(), getRadius(), color);
    }
}