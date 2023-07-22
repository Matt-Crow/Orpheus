package world.entities;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

/**
 * Instances of this class are immutable.
 */
public class ParticleGenerator {
    
    private final List<Color> colors;
    private final ParticleType type;

    /**
     * an immutable particle generator which generates no particles
     */                                                             // projectiles need at least 1 color
    public static final ParticleGenerator NONE = new ParticleGenerator(List.of(Color.BLACK), ParticleType.NONE);

    public ParticleGenerator(Collection<Color> colors, ParticleType type) {
        this.colors = List.copyOf(colors);
        this.type = type;
    }

    public List<Color> getColors() {
        return List.copyOf(colors);
    }

    public ParticleType getType() {
        return type;
    }
}
