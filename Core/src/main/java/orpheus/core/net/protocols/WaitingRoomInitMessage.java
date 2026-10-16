package orpheus.core.net.protocols;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import orpheus.core.users.User;
import orpheus.core.world.graph.utils.JsonArrayHelper;

public class WaitingRoomInitMessage {
    private final List<User> users;

    public WaitingRoomInitMessage(List<User> users){
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("team", JsonArrayHelper.toJsonArray(users, (u, jab) -> jab.add(u.toJson())))
            .build();
    }

    public static WaitingRoomInitMessage fromJson(JsonObject json) {
        var users = JsonArrayHelper.toList(json.getJsonArray("team"), (ja, i) -> User.fromJson(ja.getJsonObject(i)));
        return new WaitingRoomInitMessage(users);
    }
}
