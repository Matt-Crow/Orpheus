package orpheus.core.world.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * renders and serializes a Player
 */
public class Player extends Entity {
    private final String id;
    private final int hp;
    private final List<String> statuses;
    private final Color teamColor;
    private final Color color;

    public Player(String id, int x, int y, int radius, int hp, 
        List<String> statuses, Color teamColor, Color color) {
        super(x, y, radius);
        this.id = id;
        this.hp = hp;
        this.statuses = statuses;
        this.teamColor = teamColor;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public int getHp() {
        return hp;
    }
    
    @Override
    public void draw(Graphics g) {
        int x = getX();
        int y = getY();
        int r = getRadius();

        g.setColor(teamColor);
        g.fillOval(x - r, y - r, 2 * r, 2 * r);

        g.setColor(color);
        g.fillOval(x - 4*r/5, y - 4*r/5, (int)(r * 1.6), (int)(r * 1.6));
    
        g.setColor(Color.black);
        g.drawString(String.format("HP: %4d", hp), x - r, y - r);
        
        for (var i = 0; i < statuses.size(); i++) { // newlines don't work in g.drawString
            g.drawString(
                statuses.get(i), 
                x - r, 
                y + r + i * g.getFontMetrics().getHeight()
            );
        }
    }
    
    @Override
    public JsonObject serializeJson() {
        var serializedStatuses = Json.createArrayBuilder();
        for (var status : statuses) {
            serializedStatuses.add(status);
        }
        return Json.createObjectBuilder(super.serializeJson())
            .add("id", id)
            .add("hp", hp)
            .add("statuses", serializedStatuses)
            .add("teamColor", teamColor.getRGB())
            .add("color", color.getRGB())
            .build();
    }

    public static Player fromJson(JsonObject json) {
        var statuses = new LinkedList<String>();
        var array = json.getJsonArray("statuses");
        var s = array.size();
        for (var i = 0; i < s; i++) {
            statuses.add(array.getString(i));
        }
        return new Player(
            json.getString("id"),
            json.getInt("x"),
            json.getInt("y"),
            json.getInt("radius"),
            json.getInt("hp"),
            statuses,
            new Color(json.getInt("teamColor")),
            new Color(json.getInt("color"))
        );
    }
}