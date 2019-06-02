
package windows;

import controllers.World;
import graphics.ImageTile;
import graphics.Tile;
import controllers.Master;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
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
        WorldCanvas c = new WorldCanvas();
        JFrame f = new JFrame();
        f.setContentPane(c);
        f.setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*
        try {
            Image testImage = ImageIO.read(c.getClass().getResourceAsStream("/testImage.PNG"));
            c.world.setBlock(0, new ImageTile(0, 0, testImage));
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
        
        Tile t = new Tile(0, 0, Color.BLACK);
        t.setBlocking(true);
        
        c.world
            .setBlock(0, new Tile(0, 0, Color.WHITE))
            .setBlock(1, t)
            .setTile(0, 0, 1)
            .setTile(0, 1, 1)
            .setTile(1, 1, 1)
            .setTile(2, 1, 1)
            .setTile(2, 2, 1)
            .initTiles();
        f.setVisible(true);
        f.revalidate();
        f.repaint();
        f.addMouseListener(new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e) {
                c.world.findPath(150, 250, 150, 50).draw(c.getGraphics());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }
        
        });
        
    }
}
