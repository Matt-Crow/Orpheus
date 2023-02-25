package orpheus.core.world.graph;

import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * renders and serializes a Team
 */
public class Team implements GraphElement {

    private final Collection<Player> members; // might need entity instead
    
    public Team(Collection<Player> members) {
        this.members = members;
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
