package controllers;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import graphics.Map;
import graphics.MapLoader;
import graphics.Tile;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import serialization.JsonUtil;

/**
 *
 * @author Matt
 */
public class WorldJsonUtil {
    public static JsonObject serializeJson(World w){
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("type", "world");
        b.add("map", serializeMap(w.getMap()));
        return b.build();
    }
    
    public static JsonObject serializeMap(Map m){
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("type", "map");
        b.add("tile map", m.getCsv());
        JsonObjectBuilder tileSetBuilder = Json.createObjectBuilder();
        m.getTileSet().entrySet().stream().forEach((e)->{
            tileSetBuilder.add(e.getKey().toString(), serializeTile(e.getValue()));
        });
        b.add("tile set", tileSetBuilder.build());
        return b.build();
    }
    
    public static Map deserializeMap(JsonObject obj) throws IOException{
        JsonUtil.verify(obj, "tile map");
        JsonUtil.verify(obj, "tile set");
        InputStream in = new ByteArrayInputStream(obj.getString("tile map").getBytes());
        Map ret = MapLoader.readCsv(in);
        
        //deserialize tile set
        JsonObject tileSet = obj.getJsonObject("tile set");
        tileSet.entrySet().forEach((e)->{
            ret.addToTileSet(Integer.parseInt(e.getKey()), deserializeTile((JsonObject)e.getValue()));
        });
        return ret;
    }
    
    //todo: make this support other tile types, or just move this to tile classes
    /**
     * Serializes a template tile, so it doesn't record the actual coordinates
     * @param t the tile to serialize a coordinate-less template for
     * @return a json representation of the tile
     */
    public static JsonObject serializeTile(Tile t){
        JsonObjectBuilder b = Json.createObjectBuilder();
        b.add("type", t.getClass().toString());
        Color c = t.getColor();
        b.add("color", String.format("%d, %d, %d, %d", c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()));
        b.add("blocking", t.getBlocking());
        return b.build();
    }
    
    /**
     * Deserializes a regular Tile template stored as a json object.
     * Note that this does not account for subclasses of Tile.
     * @param obj
     * @return a coordinate-less tile, extracted from the Json Object
     */
    public static Tile deserializeTile(JsonObject obj){
        JsonUtil.verify(obj, "color");
        JsonUtil.verify(obj, "blocking");
        String[] split = obj.getString("color").split(", ");
        Color c = new Color(
            Integer.parseInt(split[0].trim()),
            Integer.parseInt(split[1].trim()),
            Integer.parseInt(split[2].trim()),
            Integer.parseInt(split[3].trim())
        );
        Tile ret = new Tile(0, 0, c);
        ret.setBlocking(obj.getBoolean("blocking"));
        
        return ret;
    }
}
