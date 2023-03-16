package orpheus.core.commands;

import javax.json.Json;
import javax.json.JsonObject;

import util.CardinalDirection;
import world.entities.HumanPlayer;
import world.World;

public class StopMoving implements Command {

    private final String playerId;
    private final CardinalDirection direction;

    public StopMoving(String playerId, CardinalDirection direction) {
        this.playerId = playerId;
        this.direction = direction;
    }

    @Override
    public void executeIn(World world) {
        var player = world.getPlayers()
            .getMemberById(playerId);
        
        ((HumanPlayer)player).setMovingInDir(direction, false);
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("type", "StopMoving")
            .add("playerId", playerId)
            .add("direction", direction.toString())
            .build();
    }

    public static StopMoving fromJson(JsonObject json) {
        return new StopMoving(
            json.getString("playerId"),
            CardinalDirection.fromString(json.getString("direction"))
        );
    }
}
