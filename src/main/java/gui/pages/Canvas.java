package gui.pages;

import gui.graphics.CustomColors;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import util.SafeList;

/**
 * This is used by WorldCanvas to render
 * Worlds. It uses most of the functionality
 * found in the old DrawingPlane class.
 * 
 * @author Matt Crow
 */
public class Canvas extends JPanel{
    private static final double ZOOM_SPEED = 0.1; 
    
    private int translateX;
    private int translateY;
    private double zoom;
    
    private Graphics2D graphics;
    private AffineTransform initialTransform;
    
    private final SafeList<EndOfFrameListener> endOfFrameListeners;
    
    /**
     * Creates a canvas, which keeps track of
     * translations and transformations
     */
    public Canvas(){
        super();
        translateX = 0;
        translateY = 0;
        zoom = 1.0;
        graphics = null;
        initialTransform = null;
        endOfFrameListeners = new SafeList<>();
        setBackground(CustomColors.black);
    }
    
    /**
     * Applies this alterations to the given Graphics object,
     * then returns it as a Graphics2D.
     * 
     * You must call this method in order for this transforms and
     * translations to apply.
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
     * this does account for translations.
     * 
     * Note that this doesn't work after reset is invoked, as then this graphics no longer
     * has this transforms applied
     * 
     * @return the x coordinate on this canvas where the mouse cursor is located
     */
    public int getMouseX(){
        Point p = getMousePosition(); //returns mouse position on this, or null if it isn't on this
        return (int) (((p == null) ? 0 : (p.x - translateX) / zoom));
    }
    
    /**
     * Returns the point on this canvas where the mouse cursor is located,
     * this does account for translations
     * 
     * Note that this doesn't work after reset is invoked, as then this graphics no longer
     * has this transforms applied
     * 
     * @return the y coordinate on this canvas where the mouse cursor is located
     */
    public int getMouseY(){
        Point p = getMousePosition();
        return (int)(((p == null) ? 0 : (p.y - translateY)) / zoom);
    }
    
    /**
     * Centers the "camera" on a given point.
     * @param x
     * @param y 
     */
    public void centerOn(int x, int y){
        translate(
            -(int)(x * getZoom() - getWidth() / 2),
            -(int)(y * getZoom() -  getHeight() / 2)
        );
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
    public final void zoomIn(){
        zoom += ZOOM_SPEED;
        if(zoom >= 5.0){
            zoom = 5.0;
        }
        repaint();
    }
    public final void zoomOut(){
        zoom -= ZOOM_SPEED;
        if(zoom <= 0.2){
            zoom = 0.2;
        }
        repaint();
    }
    
    /**
     * Resets the Graphics object this translations 
     * were applied to, so that subsequent calls to 
     * drawing on that graphics object will not be
     * resized or translated. Use this before drawing
     * items on this that should be positioned absolutely,
     * ignoring translations.
     */
    public final void reset(){
        if(graphics != null){
            graphics.setTransform(initialTransform);
        }
    }
    
    public final int getTx(){
        return translateX;
    }
    public final int getTy(){
        return translateY;
    }
    public final double getZoom(){
        return zoom;
    }
    public final void displayTransforms(){
        System.out.printf("X translation: %d\n", translateX);
        System.out.printf("Y translation: %d\n", translateY);
        System.out.printf("Zoom: %f\n", zoom);
    }
    
    public void addEndOfFrameListener(EndOfFrameListener f){
        if(f == null){
            throw new NullPointerException();
        }
        endOfFrameListeners.add(f);
    }
    public void endOfFrame(){
        endOfFrameListeners.forEach(eofl -> eofl.frameEnded(this));
    }
    
    /**
     * Registers a key control to the Canvas.
     * For example,
     * <br>
     * {@code
     *  registerKey(KeyEvent.VK_X, true, ()->foo());
     * }
     * <br>
     * will cause foo to run whenever the user presses the 'x' key.
     * 
     * @param key the KeyEvent code for the key to react to
     * @param pressed whether to react when the key is pressed or released
     * @param r the runnable to run when the key is pressed/released
     */
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
