package orpheus.core.world.graph;

import java.awt.Graphics;
import javax.json.Json;
import javax.json.JsonObject;

import orpheus.core.world.graph.particles.Particles;

/**
 * renders and serializes a World
 */
public class World implements GraphElement {

    private final Map map;
    private final Team players;
    private final Team enemies;
    private final Game game;

    public World(Map map, Team players, Team enemies, Game game) {
        this.map = map;
        this.players = players;
        this.enemies = enemies;
        this.game = game;
    }

    public void spawnParticlesInto(Particles particles) {
        players.spawnParticlesInto(particles);
        enemies.spawnParticlesInto(particles);
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
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("map", map.toJson())
            .add("players", players.toJson())
            .add("enemies", enemies.toJson())
            .add("game", game.toJson())
            .build();
    }

    public static World fromJson(JsonObject json) {
        return new World(
            Map.fromJson(json.getJsonObject("map")),
            Team.fromJson(json.getJsonObject("players")),
            Team.fromJson(json.getJsonObject("enemies")),
            Game.fromJson(json.getJsonObject("game"))
        );
    }
}
