package orpheus.core.world.graph;

import java.awt.Graphics;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * serializes a Game
 */
public class Game implements GraphElement {

    private final boolean over;

    private final boolean playersWon;

    public Game(boolean over, boolean playersWon) {
        this.over = over;
        this.playersWon = playersWon;
    }
    
    public boolean isOver() {
        return over;
    }

    public boolean isPlayerWin() {
        return playersWon;
    }

    @Override
    public void draw(Graphics g) {
        
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("over", over)
            .add("playersWon", playersWon)
            .build();
    }

    public static Game fromJson(JsonObject json) {
        return new Game(
            json.getBoolean("over"),
            json.getBoolean("playersWon")
        );
    }
}
