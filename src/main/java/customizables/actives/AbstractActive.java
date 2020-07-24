package customizables.actives;

import java.awt.Color;
import java.awt.Graphics;

import controllers.Settings;
import customizables.AbstractCustomizable;

/**
 * The AbstractActive class serves as the base for active abilities possessed by Players
 * @author Matt
 */
public abstract class AbstractActive extends AbstractCustomizable{
    /**
     * @param n the name of this active
     */
    public AbstractActive(String n){
        super(n);

        setCooldownTime(1);
    }

    /**
     * Displays information about this active on screen
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h 
     */
    public final void drawStatusPane(Graphics g, int x, int y, int w, int h){
        if(canUse()){
            g.setColor(Color.white);
            g.fillRect(x, y, w, h);
            g.setColor(Color.black);
            g.drawString(getName(), x + 10, y + 20);
        } else {
            g.setColor(Color.black);
            g.fillRect(x, y, w, h);
            if(isOnCooldown()){
                g.setColor(Color.red);
                g.drawString("On cooldown: " + Settings.framesToSeconds(getFramesUntilUse()), x + 10, y + 20);
            }
        }
    }
    
    public boolean canUse(){
        return getUser() != null && !isOnCooldown();
    }
    
    @Override
    public void init(){
        //dummy init method
    }
    
    @Override
    public final void trigger(){
        if(canUse()){
            setToCooldown();
            use();
        }
    }
    
    public abstract void use();
    
    @Override
    public void update(){
        
    }
    
    @Override
    public abstract AbstractActive copy();
}