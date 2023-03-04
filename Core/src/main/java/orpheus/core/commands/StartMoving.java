package orpheus.core.commands;

import javax.json.Json;
import javax.json.JsonObject;

import util.CardinalDirection;
import world.entities.HumanPlayer;
import world.World;

public class StartMoving implements Command {

    private final String playerId;
    private final CardinalDirection direction;

    public StartMoving(String playerId, CardinalDirection direction) {
        this.playerId = playerId;
        this.direction = direction;
    }

    @Override
    public void executeIn(World world) {
        var player = world.getPlayers()
            .getMemberById(playerId);
        
        ((HumanPlayer)player).setMovingInDir(direction, true);
    }
    
    @Override
    public JsonObject serializeJson() {
        return Json.createObjectBuilder()
            .add("type", "StartMoving")
            .add("playerId", playerId)
            .add("direction", direction.toString())
            .build();
    }

    public static StartMoving fromJson(JsonObject json) {
        return new StartMoving(
            json.getString("playerId"),
            CardinalDirection.fromString(json.getString("direction"))
        );
    }
}
