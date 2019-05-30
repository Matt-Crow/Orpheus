
package windows;

import controllers.World;
import graphics.ImageTile;
import graphics.Tile;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * Test class for now
 * @author Matt Crow
 */
public class WorldCanvas extends DrawingPlane{
    private World world;
    public WorldCanvas(){
        world = new World(100);
    }
    
    @Override
    public void paintComponent(Graphics g){
        world.draw(g);
    }
    
    public static void main(String[] args){
        JFrame f = new JFrame();
        WorldCanvas c = new WorldCanvas();
        f.setContentPane(c);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        try {
            Image testImage = ImageIO.read(c.getClass().getResourceAsStream("/testImage.PNG"));
            c.world.setBlock(0, new ImageTile(0, 0, testImage));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        c.world
            .setBlock(1, new Tile(0, 0, Color.BLACK))
            .setTile(0, 0, 1)
            .setTile(0, 1, 1)
            .setTile(0, 2, 1)
            .setTile(1, 1, 1)
            .setTile(2, 0, 1)
            .setTile(2, 1, 1)
            .setTile(2, 2, 1)
            .initTiles();
        f.setVisible(true);
        f.revalidate();
        f.repaint();
    }
}
