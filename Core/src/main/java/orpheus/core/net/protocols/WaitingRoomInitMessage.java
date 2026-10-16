package orpheus.core.net.protocols;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import orpheus.core.users.User;
import orpheus.core.utils.OrpheusJsonHelper;

public class WaitingRoomInitMessage {
    private final List<User> users;

    public WaitingRoomInitMessage(List<User> users){
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public JsonObject toJson() {
        var userJsons = users.stream()
            .map(User::toJson)
            .toList();
        return Json.createObjectBuilder()
            .add("team", OrpheusJsonHelper.toJsonArrayOfObjects(userJsons))
            .build();
    }

    public static WaitingRoomInitMessage fromJson(JsonObject json) {
        var users = OrpheusJsonHelper.toListOfObjects(json.getJsonArray("team"), User::fromJson);
        return new WaitingRoomInitMessage(users);
    }
}
