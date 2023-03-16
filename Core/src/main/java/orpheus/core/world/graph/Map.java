package orpheus.core.world.graph;

import static orpheus.core.world.graph.Tile.TILE_SIZE;

import java.awt.Graphics;
import java.util.HashMap;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * renders and serializes a 2D matrix of tiles
 */
public class Map implements GraphElement {
    private final int widthInTiles;
    private final int heightInTiles;

    /**
     * Flyweight matrix. Use tileMap[y][x], not tileMap[x][y]
     */
    private final int[][] tileMap;

    /**
     * maps values in the tileMap to Tiles to render there
     */
    private HashMap<Integer, Tile> tileSet;

    public Map(int widthInTiles, int heightInTiles) {
        this.widthInTiles = widthInTiles;
        this.heightInTiles = heightInTiles;
        tileMap = new int[heightInTiles][widthInTiles];
        tileSet = new HashMap<>();
    }

    public void addToTileSet(int key, Tile value) {
        tileSet.put(key, value);
    }

    public void setTile(int x, int y, int key) {
        // don't bother checking for out of bounds - next line handles it
        tileMap[y][x] = key;
    }

    @Override
    public void draw(Graphics g) {
        for (var yIdx = 0; yIdx < heightInTiles; yIdx++) {
            for (var xIdx = 0; xIdx < widthInTiles; xIdx++) {
                tileSet.get(tileMap[yIdx][xIdx]).drawAt(g, xIdx * TILE_SIZE, yIdx * TILE_SIZE);
            }
        }
    }
    
    @Override
    public JsonObject toJson() {
        var serializedTileMap = Json.createArrayBuilder();
        for (var y = 0; y < heightInTiles; y++) {
            var row = Json.createArrayBuilder();
            for (int x = 0; x < widthInTiles; x++) {
                row.add(tileMap[y][x]);
            }
            serializedTileMap.add(row);
        }

        var serializedTileSet = Json.createArrayBuilder();
        for (var kv : tileSet.entrySet()) {
            serializedTileSet.add(Json.createObjectBuilder()
                .add("key", kv.getKey())
                .add("value", kv.getValue().toJson())
            );
        }

        /*
         * {
         *  widthInTiles: int,
         *  heightInTiles: int,
         *  tileMap: [
         *      [int]...
         *  ],
         *  tileSet: [
         *      {
         *          key: int,
         *          value: {...}
         *      }...
         *  ]
         * }
         */
        return Json.createObjectBuilder()
            .add("widthInTiles", widthInTiles)
            .add("heightInTiles", heightInTiles)
            .add("tileMap", serializedTileMap)
            .add("tileSet", serializedTileSet)
            .build();
    }

    public static Map fromJson(JsonObject json) {
        var obj = new Map(
            json.getInt("widthInTiles"),
            json.getInt("heightInTiles")
        );

        var serializedTileMap = json.getJsonArray("tileMap");
        for (var y = 0; y < obj.heightInTiles; y++) {
            var row = serializedTileMap.getJsonArray(y);
            for (var x = 0; x < obj.widthInTiles; x++) {
                obj.setTile(x, y, row.getInt(x));
            }
        }
        
        var serializedTileSet = json.getJsonArray("tileSet");
        var size = serializedTileSet.size();
        for (var i = 0; i < size; i++) {
            var kv = serializedTileSet.getJsonObject(i);
            obj.addToTileSet(
                kv.getInt("key"),
                Tile.fromJson(kv.getJsonObject("value"))
            );
        }

        return obj;
    }
}
