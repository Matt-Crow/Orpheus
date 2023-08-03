package orpheus.core.commands;

import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;
import world.World;

public class UseActive implements Command {
    
    private final UUID playerId;
    private final TurnTo turnTo;
    private final int activeNumber;

    public UseActive(UUID playerId, TurnTo turnTo, int activeNumber) {
        this.playerId = playerId;
        this.turnTo = turnTo;
        this.activeNumber = activeNumber;
    }

    @Override
    public void executeIn(World world) {
        turnTo.executeIn(world);
        var player = world.getPlayers()
            .getMemberById(playerId);

        player.useAttack(activeNumber);
    }

    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("type", "UseActive")
            .add("playerId", playerId.toString())
            .add("turnTo", turnTo.toJson())
            .add("activeNumber", activeNumber)
            .build();
    }

    public static UseActive fromJson(JsonObject json) {
        return new UseActive(
            UUID.fromString(json.getString("playerId")),
            TurnTo.fromJson(json.getJsonObject("turnTo")),
            json.getInt("activeNumber")
        );
    }
}
