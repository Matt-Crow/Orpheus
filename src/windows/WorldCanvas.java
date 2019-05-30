
package windows;

import controllers.World;
import graphics.Tile;
import java.awt.Color;
import java.awt.Graphics;
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
        
        class BlackBlock extends Tile{
            public BlackBlock(int x, int y){
                super(x, y, Color.BLACK);
            }
        }
        class WhiteBlock extends Tile{
            public WhiteBlock(int x, int y){
                super(x, y, Color.WHITE);
            }
        }
        
        //.setBlock(0, WhiteBlock.class)
        //.setBlock(1, BlackBlock.class)
        
        c.world
            .setBlock(0, new WhiteBlock(0, 0))
            .setBlock(1, new BlackBlock(0, 0))
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
