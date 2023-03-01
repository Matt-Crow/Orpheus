package orpheus.core.world.graph.particles;

import java.awt.Graphics;
import java.util.LinkedList;

import util.Settings;

/**
 * Manages the particles presented to the client
 */
public class Particles {
    private final LinkedList<ParticleWave> waves;
    
    public Particles() {
        waves = new LinkedList<>();
        waves.add(new ParticleWave());
    }

    public void add(Particle p) {
        if (waves.isEmpty()) {
            waves.add(new ParticleWave());
        }

        var last = waves.getLast();
        if (!last.isNew()) {
            last = new ParticleWave();
            waves.add(last);
        }
        last.add(p);
    }

    public void update() {
        waves.forEach((wave) -> wave.update());
        while (!waves.isEmpty() && waves.getFirst().isDone()) {
            waves.removeFirst();
        }
    }

    public void draw(Graphics g) {
        if (!Settings.DISABLEPARTICLES) {
            waves.forEach((wave) -> wave.draw(g));
        }
    }
}
