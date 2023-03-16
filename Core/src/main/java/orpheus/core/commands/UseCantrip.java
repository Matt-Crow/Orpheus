package orpheus.core.commands;

import javax.json.Json;
import javax.json.JsonObject;

import world.World;

public class UseCantrip implements Command {

    private final String playerId;
    private final TurnTo turnTo;

    public UseCantrip(String playerId, TurnTo turnTo) {
        this.playerId = playerId;
        this.turnTo = turnTo;
    }

    @Override
    public void executeIn(World world) {
        turnTo.executeIn(world);
        world.getPlayers()
            .getMemberById(playerId)
            .useMeleeAttack();
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("type", "UseCantrip")
            .add("playerId", playerId)
            .add("turnTo", turnTo.toJson())
            .build();
    }

    public static UseCantrip fromJson(JsonObject json) {
        return new UseCantrip(
            json.getString("playerId"), 
            TurnTo.fromJson(json.getJsonObject("turnTo"))
        );
    }
}
