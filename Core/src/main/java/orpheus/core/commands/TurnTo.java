package orpheus.core.commands;

import javax.json.Json;
import javax.json.JsonObject;

import world.World;

/**
 * Use by attack commands to specify where to make the player face before 
 * attacking.
 */
public class TurnTo implements Command {
    private final String playerId;
    private final int x;
    private final int y;

    public TurnTo(String playerId, int x, int y) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
    }

    @Override
    public void executeIn(World world) {
        world.getPlayers()
            .getMemberById(playerId)
            .turnTo(x, y);
    }
    
    @Override
    public JsonObject serializeJson() {
        return Json.createObjectBuilder()
            .add("type", "TurnTo")
            .add("playerId", playerId)
            .add("x", x)
            .add("y", y)
            .build();
    }

    public static TurnTo fromJson(JsonObject json) {
        return new TurnTo(
            json.getString("playerId"),
            json.getInt("x"),
            json.getInt("y")
        );
    }
}
