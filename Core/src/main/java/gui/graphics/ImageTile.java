package gui.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import serialization.JsonUtil;

/**
 * Mostly a test file
 * @author Matt
 */
public class ImageTile extends Tile {
    private Image image;
    
    public ImageTile(int x, int y, Image i){
        super(x, y, Color.BLACK);
        image = i;
    }
    
    @Override
    public ImageTile copy(int x, int y){
        ImageTile ret = new ImageTile(x, y, image);
        ret.setBlocking(getBlocking());
        return ret;
    }
    
    @Override
    public void draw(Graphics g){
        g.drawImage(image, getX(), getY(), TILE_SIZE, TILE_SIZE, null);
    }
    
    @Override
    public JsonObject serializeJson(){
        JsonObjectBuilder b = JsonUtil.deconstruct(super.serializeJson());
        b.add("type", "image tile");
        b.add("image", "method not supported yet");
        throw new UnsupportedOperationException();
        //return b.build();
    }
    
    public static ImageTile deserializeJson(){
        throw new UnsupportedOperationException();
    }
}
