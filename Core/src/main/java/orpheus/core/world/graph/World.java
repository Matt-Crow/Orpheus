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
    private final Team players;
    private final Team enemies;
    private final Collection<Particle> particles;
    private final Game game;

    public World(Map map, Team players, Team enemies, Collection<Particle> particles, Game game) {
        this.map = map;
        this.players = players;
        this.enemies = enemies;
        this.particles = particles;
        this.game = game;
    }

    public Team getPlayers() {
        return players;
    }

    public Team getEnemies() {
        return enemies;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void draw(Graphics g) {
        map.draw(g);
        enemies.draw(g);
        players.draw(g);
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
            .add("players", players.serializeJson())
            .add("enemies", enemies.serializeJson())
            .add("particles", particlesJson)
            .add("game", game.serializeJson())
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
            Team.fromJson(json.getJsonObject("players")),
            Team.fromJson(json.getJsonObject("enemies")),
            particles,
            Game.fromJson(json.getJsonObject("game"))
        );
    }
}
