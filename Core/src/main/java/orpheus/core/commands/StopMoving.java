package orpheus.core.commands;

import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;

import util.CardinalDirection;
import world.entities.HumanPlayer;
import world.World;

public class StopMoving implements Command {

    private final UUID playerId;
    private final CardinalDirection direction;

    public StopMoving(UUID playerId, CardinalDirection direction) {
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
            .add("playerId", playerId.toString())
            .add("direction", direction.toString())
            .build();
    }

    public static StopMoving fromJson(JsonObject json) {
        return new StopMoving(
            UUID.fromString(json.getString("playerId")),
            CardinalDirection.fromString(json.getString("direction"))
        );
    }
}
