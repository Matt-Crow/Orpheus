package controllers;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import graphics.Map;

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
        return b.build();
    }
}
