package world.builds.actives;

import world.Tile;

public enum Range {
    NONE(0),
    MELEE(1),
    SHORT(3),
    MEDIUM(5),
    LONG(10);

    private final int inTiles;

    private Range(int inTiles) {
        this.inTiles = inTiles;
    }

    public int getInPixels() {
        return inTiles * Tile.TILE_SIZE;
    }

    public int getInTiles() {
        return inTiles;
    }
}
