package orpheus.core.world.graph.particles;

import java.awt.Graphics;
import java.util.LinkedList;

public class ParticleWave {
    public static final int MAX_LIFE_SPAN = 15;

    private int lifeSpanInFrames;
    private final LinkedList<Particle> particles;
    
    public ParticleWave() {
        this.lifeSpanInFrames = MAX_LIFE_SPAN;
        particles = new LinkedList<>();
    }

    public void add(Particle p) {
        particles.add(p);
    }

    public boolean isNew() {
        return lifeSpanInFrames == MAX_LIFE_SPAN;
    }

    public boolean isDone() {
        return lifeSpanInFrames == 0;
    }

    public void update() {
        particles.forEach(Particle::move);
        --lifeSpanInFrames;
    }

    public void draw(Graphics g) {
        particles.forEach((p) -> p.draw(g));
    }
}
