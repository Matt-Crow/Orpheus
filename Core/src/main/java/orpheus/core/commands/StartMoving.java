package orpheus.core.commands;

import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;

import util.CardinalDirection;
import world.World;

public class StartMoving implements Command {

    private final UUID playerId;
    private final CardinalDirection direction;

    public StartMoving(UUID playerId, CardinalDirection direction) {
        this.playerId = playerId;
        this.direction = direction;
    }

    @Override
    public void executeIn(World world) {
        world
            .getPlayers()
            .getControllerById(playerId)
            .ifPresent(ctrl -> ctrl.setMovingInDir(direction, true));
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("type", "StartMoving")
            .add("playerId", playerId.toString())
            .add("direction", direction.toString())
            .build();
    }

    public static StartMoving fromJson(JsonObject json) {
        return new StartMoving(
            UUID.fromString(json.getString("playerId")),
            CardinalDirection.fromString(json.getString("direction"))
        );
    }
}
