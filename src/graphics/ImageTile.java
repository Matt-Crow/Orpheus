package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 * Mostly a test file
 * @author Matt
 */
public class ImageTile extends Tile{
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
}
