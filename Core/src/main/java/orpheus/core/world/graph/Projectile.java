package orpheus.core.world.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import orpheus.core.world.graph.particles.Particle;
import orpheus.core.world.graph.particles.Particles;
import util.Direction;
import util.Random;
import util.Settings;
import world.entities.ParticleType;

/**
 * serializes and renders a projectile
 */
public class Projectile extends WorldOccupant {
    /**
     * The number of particles generated each frame by projectiles with a burst
     * particle type.
     */
    private static final int BURST_PARTICLES = 8;

    private final Direction facing;
    private final Color color;
    private final List<? extends Color> particleColors;
    private final ParticleType particleType;

    public Projectile(int x, int y, int radius, Direction facing, Color color, 
        List<? extends Color> particleColors, ParticleType particleType) {
        super(x, y, radius);
        this.facing = facing;
        this.color = color;
        this.particleColors = particleColors;
        this.particleType = particleType;
    }

    /**
     * creates particles and adds them to the given particles
     * @param particles the collection of particles to spawn new particles into
     */
    public void spawnParticlesInto(Particles particles) {
        switch (particleType) {
            case BURST: {
                for (var i = 0; i < BURST_PARTICLES; i++) {
                    particles.add(spawnParticle(360 * i / BURST_PARTICLES, 5));
                }
                break;
            }
            case SHEAR: {
                particles.add(spawnParticle(-45, 5));
                particles.add(spawnParticle(45, 5));
                break;
            }
            case BEAM: {
                particles.add(spawnParticle(180, 5));
                break;
            }
            case BLADE: {
                particles.add(spawnParticle(0, 0));
                break;
            }
            case NONE: {
                break;
            }
            default: {
                throw new RuntimeException("Invalid particle type: " + particleType.toString());
            }
        }
    }

    private Particle spawnParticle(int angleOffset, int speed) {
        var c = particleColors.get(Random.choose(0, particleColors.size() - 1));
        return new Particle(
            getX(),
            getY(),
            speed,
            Direction.fromDegrees(facing.getDegrees() + angleOffset),
            c
        );
    }

    @Override
    public void draw(Graphics g) {
        if (Settings.DISABLEPARTICLES || particleType == ParticleType.NONE) {
            int r = getRadius();
            g.setColor(color);
            g.fillOval(getX()-r, getY()-r, 2*r, 2*r);
        }
    }
    
    @Override
    public JsonObject toJson() {
        var array = Json.createArrayBuilder();
        for (var color : particleColors) {
            array.add(color.getRGB());
        }
        return Json.createObjectBuilder(super.toJson())
            .add("facing", facing.getDegrees())
            .add("color", color.getRGB())
            .add("particleColors", array)
            .add("particleType", particleType.toString())
            .build();
    }

    public static Projectile fromJson(JsonObject json) {
        var particleColors = new LinkedList<Color>();
        var array = json.getJsonArray("particleColors");
        for (var i = 0; i < array.size(); i++) {
            particleColors.add(new Color(array.getInt(i)));
        }
        
        return new Projectile(
            json.getInt("x"),
            json.getInt("y"),
            json.getInt("radius"),
            Direction.fromDegrees(json.getInt("facing")),
            new Color(json.getInt("color")),
            particleColors,
            ParticleType.fromString(json.getString("particleType"))
        );
    }
}
