package orpheus.core.world.graph;

import java.awt.Graphics;

import javax.json.JsonObject;

/**
 * renders and serializes a Player
 */
public class Player implements GraphElement {

    public Player() {

    }

    @Override
    public void draw(Graphics g) {
        // TODO Auto-generated method stub
    }
    
    @Override
    public JsonObject serializeJson() {
        // TODO Auto-generated method stub
        return null;
    }

    public static Player fromJson(JsonObject json) {
        return new Player();
    }
}
