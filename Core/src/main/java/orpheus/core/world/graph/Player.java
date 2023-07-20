package orpheus.core.world.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;

import orpheus.core.world.graph.playables.Playable;
import orpheus.core.world.graph.playables.PlayableJsonDeserializer;
import orpheus.core.world.graph.utils.JsonArrayHelper;

/**
 * renders and serializes a Player
 */
public class Player extends WorldOccupant {
    private final UUID id;
    private final int hp;
    private final List<String> statuses;
    private final Color teamColor;
    private final Color color;
    private final Playable playingAs;
    private final List<Active> actives;

    public Player(UUID id, int x, int y, int radius, int hp, 
        List<String> statuses, Color teamColor, Color color, 
        Playable playingAs, List<Active> actives
    ) {
        
        super(x, y, radius);
        this.id = id;
        this.hp = hp;
        this.statuses = statuses;
        this.teamColor = teamColor;
        this.color = color;
        this.playingAs = playingAs;
        this.actives = actives;
    }

    public UUID getId() {
        return id;
    }

    public int getHp() {
        return hp;
    }

    public List<Active> getActives() {
        return actives;
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

        playingAs.drawAt(g, x, y);
    }
    
    @Override
    public JsonObject toJson() {
        return Json.createObjectBuilder(super.toJson())
            .add("id", id.toString())
            .add("hp", hp)
            .add("statuses", JsonArrayHelper.fromStrings(statuses))
            .add("teamColor", teamColor.getRGB())
            .add("color", color.getRGB())
            .add("playingAs", playingAs.toJson())
            .add("actives", JsonArrayHelper.fromGraphElements(actives))
            .build();
    }

    public static Player fromJson(JsonObject json) {
        return new Player(
            UUID.fromString(json.getString("id")),
            json.getInt("x"),
            json.getInt("y"),
            json.getInt("radius"),
            json.getInt("hp"),
            JsonArrayHelper.toStrings(json.getJsonArray("statuses")),
            new Color(json.getInt("teamColor")),
            new Color(json.getInt("color")),
            PlayableJsonDeserializer.fromJson(json.getJsonObject("playingAs")),
            JsonArrayHelper.toGraphElements(json.getJsonArray("actives"), Active::fromJson)
        );
    }
}
