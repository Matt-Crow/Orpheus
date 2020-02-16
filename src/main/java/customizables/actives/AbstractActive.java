package customizables.actives;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import graphics.CustomColors;

import controllers.Master;
import customizables.AbstractCustomizable;
import entities.*;
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
    
    private final int arcLength;
    private final int projectileRange;
    private final int areaOfEffect;
    private final int projectileSpeed;
    private final int damage;
    
    private final int baseArcLength;
    private final int baseProjRange;
    private final int baseAreaOfEffect;
    private final int baseProjectileSpeed;
    private final int baseDamage;
        
    private static int nextUseId = 0; // How many actives have been used thus far. Used to prevent double hitting

    /**
     * 
     * @param t the type of active ability this is. Used for JSON deserialization
     * @param n the name of this active
     * 
     * @param arcLength an integer from 1 to 5, denoting how wide of an arc of
     * projectiles this will spawn upon trigger: 1: 45 2: 90 3: 135 4: 180 5:
     * 360
     *
     * @param range 0 to 5. how far the projectiles will travel before
     * terminating
     * @param speed 1 to 5. how fast the projectile moves
     * @param aoe 0 to 5. Upon terminating, if this has an aoe (not 0), 
     * the terminating projectile will explode into other projectiles,
     * which will travel a distance calculated from this aoe.
     * 
     * @param dmg 0 to 5. Upon colliding with a player, this' projectiles will inflict a total of
     * (dmg * (5% of average character's HP)) damage.
     */
    public AbstractActive(ActiveType t, String n, int arcLength, int range, int speed, int aoe, int dmg){
        super(n);
        type = t;

        baseArcLength = Number.minMax(0, arcLength, 5);
        baseProjRange = Number.minMax(0, range, 5);
        baseProjectileSpeed = Number.minMax(1, speed, 5);
        baseAreaOfEffect = Number.minMax(0, aoe, 5);
        baseDamage = Number.minMax(0, dmg, 5);
        
        this.arcLength = (baseArcLength == 5) ? 360 : baseArcLength * 45;
        int rng = 0;
        for(int i = 1; i <= baseProjRange; i++){
            rng += i;
        }
        projectileRange = rng * Master.UNITSIZE;
        projectileSpeed = Master.UNITSIZE * baseProjectileSpeed / Master.FPS;
        areaOfEffect = baseAreaOfEffect * Master.UNITSIZE;
        damage = baseDamage * CharacterClass.BASE_HP / 20;

        particleType = ParticleType.NONE;
        colors = new CustomColors[]{CustomColors.black};
        setCooldownTime(1);

        tags = new ArrayList<>();
    }
    
    public final int getArcLength(){
        return arcLength;
    }
    public final int getRange(){
        return projectileRange;
    }
    public final int getSpeed(){
        return projectileSpeed;
    }
    public final int getAOE(){
        return areaOfEffect;
    }
    public final int getDamage(){
        return damage;
    }
    
    
    public final int getBaseArcLength(){
        return baseArcLength;
    }
    public final int getBaseRange(){
        return baseProjRange;
    }
    public final int getBaseProjectileSpeed(){
        return baseProjectileSpeed;
    }
    public final int getBaseAreaOfEffect(){
        return areaOfEffect;
    }
    public final int getBaseDamage(){
        return baseDamage;
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

    /**
     * Creates a projectile at this' user's coordinates
     * @param facingDegrees the direction the new projectile will travel
     */
    private void spawnProjectile(int facingDegrees){
        getUser().spawn(new SeedProjectile(nextUseId, getUser().getX(), getUser().getY(), facingDegrees, projectileSpeed, getUser(), this));    
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
    
    @Override
    public void trigger(){
        consumeEnergy();
        if(type != ActiveType.BOOST){
            spawnArc(arcLength);
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
            damage
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
        user.getActionRegister().triggerOnHit(p);
        p.getActionRegister().triggerOnHitReceived(user);
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
    public void update(){
        
    }
    
    @Override
    public abstract AbstractActive copy();
}