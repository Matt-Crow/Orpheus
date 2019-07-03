package windows;

import graphics.CustomColors;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * This is used by WorldCanvas to render
 * Worlds. It uses most of the functionality
 * found in the old DrawingPlane class.
 * 
 * @author Matt Crow
 */
public class Canvas extends JPanel{
    private int translateX;
    private int translateY;
    
    //previous translations before calling reset
    private int priorTx;
    private int priorTy;
    
    private double zoom;
    private double priorZoom;
    
    private Graphics2D graphics;
    private AffineTransform initialTransform;
    
    public Canvas(){
        super();
        translateX = 0;
        translateY = 0;
        priorTx = 0;
        priorTy = 0;
        zoom = 1.0;
        priorZoom = 1.0;
        graphics = null;
        initialTransform = null;
        setBackground(CustomColors.black);
    }
    
    /**
     * Applies this alterations to the given Graphics object,
     * then returns it as a Graphics2D.
     * 
     * @param g the graphics to apply this transforms to
     * @return the newly transformed graphics
     */
    public final Graphics2D applyTransforms(Graphics g){
        graphics = (Graphics2D)g;
        initialTransform = graphics.getTransform();
        graphics.translate(translateX, translateY);
        graphics.scale(zoom, zoom);
        return graphics;
    }
    
    /**
     * Returns the point on this canvas where the mouse cursor is located,
     * this does account for translations
     * @return the x coordinate on this canvas where the mouse cursor is located
     */
    public int getMouseX(){
        Point p = getMousePosition(); //returns mouse position on this, or null if it isn't on this
        return (int) (((p == null) ? 0 : (p.x - getPriorTx()) / priorZoom));
    }
    
    /**
     * Returns the point on this canvas where the mouse cursor is located,
     * this does account for translations
     * @return the y coordinate on this canvas where the mouse cursor is located
     */
    public int getMouseY(){
        Point p = getMousePosition();
        return (int)(((p == null) ? 0 : (p.y - getPriorTy())) / priorZoom);
    }
    
    public final void translate(int x, int y){
        translateX = x;
        translateY = y;
        if(graphics != null){
            graphics.translate(x, y);
        }
    }
    public final void setZoom(double z){
        zoom = z;
        if(graphics != null){
            graphics.scale(z, z);
        }
    }
    
    public final void reset(){
        
        priorTx = translateX;
        priorTy = translateY;
        priorZoom = zoom;
        
        translateX = 0;
        translateY = 0;
        zoom = 1.0;
        
        if(graphics != null){
            graphics.setTransform(initialTransform);
        }
    }
    
    public final int getTx(){
        return translateX;
    }
    public final int getPriorTx(){
        return priorTx;
    }
    public final int getTy(){
        return translateY;
    }
    public final int getPriorTy(){
        return priorTy;
    }
    public final double getZoom(){
        return zoom;
    }
    public final double getPriorZoom(){
        return priorZoom;
    }
    public final void displayTransforms(){
        System.out.printf("X translation: %d\n", translateX);
        System.out.printf("Y translation: %d\n", translateY);
        System.out.printf("Zoom: %f\n", zoom);
    }
    
    public void registerKey(int key, boolean pressed, Runnable r){
        String text = key + ((pressed) ? " pressed" : " released");
        getInputMap().put(KeyStroke.getKeyStroke(key, 0, !pressed), text);
        getActionMap().put(text, new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                r.run();
            }
        });
    }
}
