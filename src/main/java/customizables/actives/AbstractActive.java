package customizables.actives;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import graphics.CustomColors;

import controllers.Master;
import customizables.AbstractCustomizable;
import entities.*;

/**
 * The AbstractActive class serves as the base for active abilities possessed by Players
 * @author Matt
 */
public abstract class AbstractActive extends AbstractCustomizable{
    private final ActiveType type; // used for serialization
    private int cost; // the energy cost of the active. Calculated automatically
    private final ArrayList<ActiveTag> tags; //tags are used to modify this' behaviour. Only once is currently implemented 
    
    /**
     * 
     * @param t the type of active ability this is. Used for JSON deserialization
     * @param n the name of this active
     */
    public AbstractActive(ActiveType t, String n){
        super(n);
        type = t;

        setCooldownTime(1);

        tags = new ArrayList<>();
    }
    
    public final ActiveType getActiveType(){
        return type;
    }
    public final int getCost(){
        return cost;
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
    public final boolean canUse(){
        AbstractPlayer user = getUser();
        boolean ret = !isOnCooldown();
        if(user == null){
            ret = false;
        } else if(ret && user instanceof HumanPlayer){
            ret = ((HumanPlayer)user).getEnergyLog().getEnergy() >= cost;
        } else if(ret && cost <= 0){
            ret = true;
        }
        // AI can only trigger if it has no cost
        return ret;
    }

    
    
    private void consumeEnergy(){
        if(getUser() instanceof HumanPlayer){
            ((HumanPlayer)getUser()).getEnergyLog().loseEnergy(cost);
        }
        setToCooldown();
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
        if(!isOnCooldown()){
            g.setColor(Color.white);
            g.fillRect(x, y, w, h);
            g.setColor(Color.black);
            g.drawString(getName(), x + 10, y + 20);
        } else {
            g.setColor(Color.black);
            g.fillRect(x, y, w, h);
            g.setColor(Color.red);
            g.drawString("On cooldown: " + Master.framesToSeconds(getFramesUntilUse()), x + 10, y + 20);
        }
        if(canUse()){
            g.setColor(CustomColors.green);
        } else {
            g.setColor(CustomColors.red);
        }
        if(getUser() instanceof HumanPlayer){
            g.drawString("Energy cost: " + ((HumanPlayer)getUser()).getEnergyLog().getEnergy() + "/" + cost, x + 10, y + 33);
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