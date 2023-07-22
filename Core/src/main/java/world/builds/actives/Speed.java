package world.builds.actives;

import util.Settings;
import world.Tile;

public enum Speed {
    IMMOBILE(0),
    SLOW(2),
    MEDIUM(3),
    FAST(5);

    private final int tilesPerSecond;

    private Speed(int tilesPerSecond) {
        this.tilesPerSecond = tilesPerSecond;
    }  

    public int getInTilesPerSecond() {
        return tilesPerSecond;
    }

    public double getInPixelsPerFrame() {
        return ((double)tilesPerSecond) * Tile.TILE_SIZE / Settings.FPS;
    }
}
