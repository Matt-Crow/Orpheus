
package windows;

import controllers.World;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

/**
 *
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
        c.world.setTileSize(100)
            .setColor(0, Color.BLACK)
            .setColor(1, Color.WHITE)
            .setTile(0, 0, 1)
            .setTile(0, 1, 1)
            .setTile(0, 2, 1)
            .setTile(1, 1, 1)
            .setTile(2, 0, 1)
            .setTile(2, 1, 1)
            .setTile(2, 2, 1);
        f.setVisible(true);
        f.revalidate();
        f.repaint();
    }
}
