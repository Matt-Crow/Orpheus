package customizables.actives;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import graphics.CustomColors;

import controllers.Master;
import customizables.AbstractCustomizable;
import entities.*;
import statuses.*;
import customizables.CustomizableType;
import customizables.characterClass.CharacterClass;
import customizables.characterClass.CharacterStatName;
import util.Number;

/**
 * The AbstractActive class serves as the base for active abilities possessed by Players
 * @author Matt
 */
public abstract class AbstractActive extends AbstractCustomizable{
    private final ActiveType type; // used for serialization
    private ParticleType particleType; // the type of particles this' projectiles emit @see Projectile
    private CustomColors[] colors;
    private int cost; // the energy cost of the active. Calculated automatically
    private final ArrayList<ActiveTag> tags; //tags are used to modify this' behaviour. Only once is currently implemented 
    
    private static int nextUseId = 0; // How many actives have been used thus far. Used to prevent double hitting

    /**
     * 
     * @param t the type of active ability this is. Used for JSON deserialization
     * @param n the name of this active
     * 
     * @param arcLength an integer from 1 to 5, denoting how wide of an arc of projectiles this will spawn upon use:
     * 1: 45
     * 2: 90
     * 3: 135
     * 4: 180
     * 5: 360
     * 
     * @param range 0 to 5. how far the projectiles will travel before terminating
     * @param speed 1 to 5. how fast the projectile moves
     * @param aoe 0 to 5. Upon terminating, if this has an aoe (not 0), 
     * the terminating projectile will explode into other projectiles,
     * which will travel a distance calculated from this aoe.
     * 
     * @param dmg 0 to 5. Upon colliding with a player, this' projectiles will inflict a total of
     * (dmg * (5% of average character's HP)) damage.
     */
    public AbstractActive(ActiveType t, String n, int arcLength, int range, int speed, int aoe, int dmg){
        super(CustomizableType.ACTIVE, n);
        type = t;

        setStat(ActiveStatName.ARC, arcLength);
        setStat(ActiveStatName.RANGE, range);
        setStat(ActiveStatName.SPEED, speed);
        setStat(ActiveStatName.AOE, aoe);
        setStat(ActiveStatName.DAMAGE, dmg);

        particleType = ParticleType.NONE;
        colors = new CustomColors[]{CustomColors.black};
        setCooldown(1);

        tags = new ArrayList<>();
    }
    
    public void setColors(CustomColors[] cs){
        colors = cs.clone();
    }
    public CustomColors[] getColors(){
        return colors;
    }
    
    public final ActiveType getActiveType(){
        return type;
    }
    public final int getCost(){
        return cost;
    }

    /**
     * Uses my 1 to 5 stat system:
     * 1: weak
     * 2: subpar
     * 3: average
     * 4: above average
     * 5: strong
     * 
     * @param n
     * @param value 
     */
    public void setStat(ActiveStatName n, int value){
        // 1-5 stat system
        switch(n){
        case ARC:
            // 0 - 360 degrees
            /*
             * 1: 45
             * 2: 90
             * 3: 135
             * 4: 180
             * 5: 360
             */
            value = Number.minMax(0, value, 5);
            int deg = value * 45;
            if(value == 0){
                    deg = 1;
            } else if(value == 5){
                    deg = 360;
            }
            addStat(ActiveStatName.ARC, value, deg);
            break;
        case RANGE:
            // 1-15 units of range. Increases exponentially
            int units = 0;
            value = Number.minMax(0, value, 5);
            for(int i = 0; i <= value; i++){
                    units += i;
            }
            addStat(ActiveStatName.RANGE, value, units * 100);
            break;
        case SPEED:
            // 1-5 units per second
            value = Number.minMax(1, value, 5);
            addStat(ActiveStatName.SPEED, value, 100 * value / Master.FPS);
            break;
        case AOE:
            // 1-5 units (or 0)
            value = Number.minMax(0, value, 5);
            addStat(ActiveStatName.AOE, value, value * Master.UNITSIZE);
            break;
        case DAMAGE:
            // 5% to 25% of the average character's HP
            value = Number.minMax(1, value, 5);
            addStat(ActiveStatName.DAMAGE, value, value * CharacterClass.BASE_HP / 20);
            break;
        }
        calculateCost();
    }
    private void calculateCost(){
        int newCost = 0;
        if(type != ActiveType.MELEE){
            int[] bases = getAllBaseValues();
            for(int i = 0; i < bases.length; i++){
                newCost += bases[i];
            }
            
            int statusCost = 0;
            getInflict()
                .stream()
                .map((status) -> status.getBaseParam() + status.getIntensityLevel())
                .reduce(statusCost, Integer::sum);
            newCost += statusCost;
        }
        cost = newCost;
    }

    // particle methods
    public void setParticleType(ParticleType t){
        particleType = t;
    }
    public ParticleType getParticleType(){
        return particleType;
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
        boolean ret = !onCooldown();
        if(user == null){
            ret = false;
        } else if(ret && user instanceof HumanPlayer){
            ret = ((HumanPlayer)user).getEnergyLog().getEnergy() >= cost;
        } else if(ret && cost <= 0){
            ret = true;
        }
        // AI can only use if it has no cost
        return ret;
    }

    /**
     * Creates a projectile at this' user's coordinates
     * @param facingDegrees the direction the new projectile will travel
     */
    private void spawnProjectile(int facingDegrees){
        getUser().spawn(new SeedProjectile(nextUseId, getUser().getX(), getUser().getY(), facingDegrees, (int) getStatValue(ActiveStatName.SPEED), getUser(), this));    
    }
    
    /**
     * Generates an arc of projectiles from this' user
     * @param arcDegrees the number of degrees in the arc
     */
    private void spawnArc(int arcDegrees){
        int start = getUser().getDir().getDegrees() - arcDegrees / 2;
        // spawn projectiles every 15 degrees
        for(int angleOffset = 0; angleOffset < arcDegrees; angleOffset += 15){
            spawnProjectile(start + angleOffset);
        }
    }
    
    private void consumeEnergy(){
        if(getUser() instanceof HumanPlayer){
            ((HumanPlayer)getUser()).getEnergyLog().loseEnergy(cost);
        }
        setToCooldown();
    }
    
    public void use(){
        consumeEnergy();
        if(type != ActiveType.BOOST){
            spawnArc((int)getStatValue(ActiveStatName.ARC));
            nextUseId++;
        }
    }
    
    /**
     * Calculates the damage this should
     * inflict on the given player upon hitting
     * them.
     * 
     * @param p
     * @return 
     */
    public int calcDmg(AbstractPlayer p){
        return (int)(
            getStatValue(ActiveStatName.DAMAGE)
            * getUser().getStatValue(CharacterStatName.DMG)
            / p.getStatValue(CharacterStatName.REDUCTION)
        );
    }
    
    /**
     * Invoked by Projectile upon 
     * colliding with a player
     * @param p the AbstractPlayer who one of this Projectiles hit.
     */
    public void hit(AbstractPlayer p){
        AbstractPlayer user = getUser();
        p.logDamage(calcDmg(p));
        if(this instanceof MeleeActive){
            user.getActionRegister().tripOnMeleeHit(p);
            if(user instanceof HumanPlayer){
                ((HumanPlayer)user).getEnergyLog().gainEnergyPerc(15.0);
            }
            p.getActionRegister().tripOnBeMeleeHit(user);
        } else {
            user.getActionRegister().triggerOnHit(p);
            p.getActionRegister().triggerOnHitReceived(user);
        }
        applyEffect(p);
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
        if(!onCooldown()){
            g.setColor(Color.white);
            g.fillRect(x, y, w, h);
            g.setColor(Color.black);
            g.drawString(getName(), x + 10, y + 20);
        } else {
            g.setColor(Color.black);
            g.fillRect(x, y, w, h);
            g.setColor(Color.red);
            g.drawString("On cooldown: " + Master.framesToSeconds(getCooldown()), x + 10, y + 20);
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
    public abstract AbstractActive copy();
}