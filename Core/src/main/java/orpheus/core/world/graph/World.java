package orpheus.core.world.graph;

import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;

import javax.json.Json;
import javax.json.JsonObject;

import util.Settings;

/**
 * renders and serializes a World
 */
public class World implements GraphElement {

    private final Map map;
    private final Collection<Particle> particles;

    public World(Map map, Collection<Particle> particles) {
        this.map = map;
        this.particles = particles;
    }

    @Override
    public void draw(Graphics g) {
        map.draw(g);

        if (!Settings.DISABLEPARTICLES) {
            particles.forEach((p) -> p.draw(g));
        }
    }
    
    @Override
    public JsonObject serializeJson() {
        var particlesJson = Json.createArrayBuilder();
        for (var particle : particles) {
            particlesJson.add(particle.serializeJson());
        }

        return Json.createObjectBuilder()
            .add("map", map.serializeJson())
            .add("particles", particlesJson)
            .build();
    }

    public static World fromJson(JsonObject json) {
        var particles = new LinkedList<Particle>();
        var array = json.getJsonArray("particles");
        var s = array.size();
        for (var i = 0; i < s; i++) {
            particles.add(Particle.fromJson(array.getJsonObject(i)));
        }

        return new World(
            Map.fromJson(json.getJsonObject("map")),
            particles
        );
    }
}
