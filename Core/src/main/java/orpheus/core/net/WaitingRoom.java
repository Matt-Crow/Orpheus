package orpheus.core.net;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;

import orpheus.core.users.User;
import serialization.JsonSerialable;

/**
 * a room which players connect to before playing a game
 */
public class WaitingRoom implements JsonSerialable {
    
    /**
     * the IP address of the server this is running on
     */
    private final String ip;

    /**
     * the port of the server this is running on
     */
    private final int port;

    /**
     * the users in the waiting room
     */
    private final List<User> players;

    public WaitingRoom(String ip, int port) {
        this.ip = ip;
        this.port = port;
        players = new ArrayList<>();
    }

    /**
     * adds a new user to the list of users
     * @param user the new user
     */
    public void addUser(User user) {
        players.add(user);
    }

    @Override
    public JsonObject serializeJson() {
        var playersJson = Json.createArrayBuilder();
        for (var user : players) {
            playersJson.add(user.serializeJson());
        }
        return Json.createObjectBuilder()
            .add("ip", ip)
            .add("port", port)
            .add("players", playersJson)
            .build();
    }

    public static WaitingRoom fromJson(JsonObject json) {
        var room = new WaitingRoom(
            json.getString("ip"),
            json.getInt("port")
        );

        json.getJsonArray("players")
            .stream()
            .map(JsonValue::asJsonObject)
            .map(User::fromJson)
            .forEach(room::addUser);

        return room;
    }
}
