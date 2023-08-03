package orpheus.core.world.graph.playables;

import java.awt.Graphics;

import javax.json.Json;
import javax.json.JsonObject;

public class AssembledBuild implements Playable {

    @Override
    public void drawAt(Graphics g, int x, int y) {
        return;
    }

    @Override
    public JsonObject toJson() {
        var json = Json.createObjectBuilder()
            .add("type", "AssembledBuild")
            .build();
        return json;
    }

    @Override
    public void draw(Graphics g) {
        return;
    }
    
}
