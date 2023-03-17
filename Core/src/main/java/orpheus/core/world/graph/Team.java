package orpheus.core.world.graph;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;

import orpheus.core.world.graph.particles.Particles;

/**
 * renders and serializes a Team
 */
public class Team implements GraphElement {

    private final List<Player> members;
    private final List<Projectile> projectiles;
    
    public Team(List<Player> members, List<Projectile> projectiles) {
        this.members = members;
        this.projectiles = projectiles;
    }

    public Optional<Player> getMemberById(UUID id) {
        return members.stream().filter((p) -> p.getId().equals(id)).findFirst();
    }

    public void spawnParticlesInto(Particles particles) {
        projectiles.forEach((projectile) -> projectile.spawnParticlesInto(particles));
    }

    @Override
    public void draw(Graphics g) {
        members.forEach((player) -> player.draw(g));
        projectiles.forEach((projectile) -> projectile.draw(g));
    }
    
    @Override
    public JsonObject toJson() {
        var serializedMembers = Json.createArrayBuilder();
        for (var member : members) {
            serializedMembers.add(member.toJson());
        }

        var serializedProjectiles = Json.createArrayBuilder();
        for (var projectile : projectiles) {
            serializedProjectiles.add(projectile.toJson());
        }

        return Json.createObjectBuilder()
            .add("members", serializedMembers)
            .add("projectiles", serializedProjectiles)
            .build();
    }

    public static Team fromJson(JsonObject json) {
        var members = new LinkedList<Player>();
        var memberArray = json.getJsonArray("members");
        for (var i = 0; i < memberArray.size(); i++) {
            members.add(Player.fromJson(memberArray.getJsonObject(i)));
        }

        var projectiles = new LinkedList<Projectile>();
        var projectileArray = json.getJsonArray("projectiles");
        for (var i = 0; i < projectileArray.size(); i++) {
            projectiles.add(Projectile.fromJson(projectileArray.getJsonObject(i)));
        }

        return new Team(members, projectiles);
    }
}
