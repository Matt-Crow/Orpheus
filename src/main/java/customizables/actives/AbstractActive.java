package customizables.actives;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;

import controllers.Master;
import customizables.AbstractCustomizable;

/**
 * The AbstractActive class serves as the base for active abilities possessed by Players
 * @author Matt
 */
public abstract class AbstractActive extends AbstractCustomizable{
    private final ArrayList<ActiveTag> tags; //tags are used to modify this' behaviour. Only one is currently implemented 
    
    /**
     * 
     * @param n the name of this active
     */
    public AbstractActive(String n){
        super(n);

        setCooldownTime(1);

        tags = new ArrayList<>();
    }
    
    public final void addTag(ActiveTag t){
        tags.add(t);
    }
    public void copyTagsTo(AbstractActive a){
        tags.forEach((t) -> {
            a.addTag(t);
        });
    }
    public boolean containsTag(ActiveTag t){
        return tags.contains(t);
    }
    public ActiveTag[] getTags(){
        return tags.toArray(new ActiveTag[tags.size()]);
    }

    // in battle methods
    public boolean canUse(){
        return getUser() != null && !isOnCooldown();
    }
    
    /**
     * Displays information about this active on screen
     * @param g
     * @param x
     * @param y
     * @param w
     * @param h 
     */
    public void drawStatusPane(Graphics g, int x, int y, int w, int h){
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
                g.drawString("On cooldown: " + Master.framesToSeconds(getFramesUntilUse()), x + 10, y + 20);
            }
        }
    }
    
    @Override
    public void init(){
        //dummy init method
    }
    
    @Override
    public void trigger(){
        setToCooldown();
    }
    
    @Override
    public void update(){
        
    }
    
    @Override
    public abstract AbstractActive copy();
}