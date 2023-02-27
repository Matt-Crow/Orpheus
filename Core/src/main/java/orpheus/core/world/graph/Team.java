package orpheus.core.world.graph;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * renders and serializes a Team
 */
public class Team implements GraphElement {

    private final List<Player> members; // might need entity instead
    
    public Team(List<Player> members) {
        this.members = members;
    }

    public Optional<Player> getMemberById(String id) {
        return members.stream().filter((p) -> p.getId().equals(id)).findFirst();
    }

    @Override
    public void draw(Graphics g) {
        members.forEach((player) -> player.draw(g));
    }
    
    @Override
    public JsonObject serializeJson() {
        var serializedMembers = Json.createArrayBuilder();
        for (var member : members) {
            serializedMembers.add(member.serializeJson());
        }

        return Json.createObjectBuilder()
            .add("members", serializedMembers)
            .build();
    }

    public static Team fromJson(JsonObject json) {
        var members = new LinkedList<Player>();
        var array = json.getJsonArray("members");
        var s = array.size();
        for (var i = 0; i < s; i++) {
            members.add(Player.fromJson(array.getJsonObject(i)));
        }

        return new Team(members);
    }
}
