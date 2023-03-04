package orpheus.core.commands;

import javax.json.Json;
import javax.json.JsonObject;
import world.entities.HumanPlayer;
import world.World;

public class UseActive implements Command {
    
    private final String playerId;
    private final TurnTo turnTo;
    private final int activeNumber;

    public UseActive(String playerId, TurnTo turnTo, int activeNumber) {
        this.playerId = playerId;
        this.turnTo = turnTo;
        this.activeNumber = activeNumber;
    }

    @Override
    public void executeIn(World world) {
        turnTo.executeIn(world);
        var player = world.getPlayers()
            .getMemberById(playerId);

        ((HumanPlayer)player).useAttack(activeNumber);
    }

    @Override
    public JsonObject serializeJson() {
        return Json.createObjectBuilder()
            .add("type", "UseActive")
            .add("playerId", playerId)
            .add("turnTo", turnTo.serializeJson())
            .add("activeNumber", activeNumber)
            .build();
    }

    public static UseActive fromJson(JsonObject json) {
        return new UseActive(
            json.getString("playerId"),
            TurnTo.fromJson(json.getJsonObject("turnTo")),
            json.getInt("activeNumber")
        );
    }
}
