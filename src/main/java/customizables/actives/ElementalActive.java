package customizables.actives;

import controllers.Settings;
import customizables.characterClass.CharacterClass;
import customizables.characterClass.CharacterStatName;
import entities.AbstractPlayer;
import entities.ParticleType;
import entities.Projectile;
import entities.SeedProjectile;
import gui.graphics.CustomColors;
import gui.graphics.Tile;

public class ElementalActive extends AbstractActive{
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
    
    
    private ParticleType particleType; // the type of particles this' projectiles emit @see Projectile
    private CustomColors[] colors;
    
    private static int nextUseId = 0; // How many actives have been used thus far. Used to prevent double hitting

    /**
     * @param n the name of this active
     * 
     * @param arc an integer from 1 to 5, denoting how wide of an arc of
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
    public ElementalActive(String n, int arc, int range, int speed, int aoe, int dmg){
		super(n);
        
        baseArcLength = util.Number.minMax(0, arc, 5);
        baseProjRange = util.Number.minMax(0, range, 5);
        baseProjectileSpeed = util.Number.minMax(1, speed, 5);
        baseAreaOfEffect = util.Number.minMax(0, aoe, 5);
        baseDamage = util.Number.minMax(0, dmg, 5);
        
        this.arcLength = (baseArcLength == 5) ? 360 : baseArcLength * 45;
        int rng = 0;
        for(int i = 1; i <= baseProjRange; i++){
            rng += i;
        }
        projectileRange = rng * Tile.TILE_SIZE;
        projectileSpeed = Tile.TILE_SIZE * baseProjectileSpeed / Settings.FPS;
        areaOfEffect = baseAreaOfEffect * Tile.TILE_SIZE;
        damage = baseDamage * CharacterClass.BASE_HP / 20;
        
        particleType = ParticleType.NONE;
        colors = new CustomColors[]{CustomColors.black};
	}
    
    @Override
	public ElementalActive copy(){
		ElementalActive copy = new ElementalActive(
				getName(), 
				getBaseArcLength(), 
				getBaseRange(), 
				getBaseProjectileSpeed(), 
				getBaseAreaOfEffect(), 
				getBaseDamage());
		copy.setParticleType(getParticleType());
        copy.setColors(getColors());
        copyInflictTo(copy);
        
		return copy;
	}
    
    public void setColors(CustomColors[] cs){
        colors = cs.clone();
    }
    public CustomColors[] getColors(){
        return colors;
    }
    
    // particle methods
    public void setParticleType(ParticleType t){
        particleType = t;
    }
    public ParticleType getParticleType(){
        return particleType;
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
    
    
    /**
     * Creates a projectile at this' user's coordinates
     * @param facingDegrees the direction the new projectile will travel
     */
    private void spawnProjectile(int facingDegrees){
        SeedProjectile p = createProjectile();
        p.setFacing(facingDegrees);
        getUser().spawn(p);    
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
     * @param hittingProjectile the Projectile which hit p
     * @param p the AbstractPlayer who one of this Projectiles hit.
     */
    public void hit(Projectile hittingProjectile, AbstractPlayer p){
        AbstractPlayer user = getUser();
        p.logDamage(calcDmg(p));
        user.getActionRegister().triggerOnHit(p);
        p.getActionRegister().triggerOnHitReceived(user);
        applyEffect(p);
    }
    
    @Override
    public void use(){
        spawnArc(arcLength);
        nextUseId++;
    }
	
    @Override
	public String getDescription(){
        StringBuilder desc = new StringBuilder();
		
        desc
            .append(getName())
            .append(": \n");
		if(getRange() == 0){
            desc.append(String.format("The user generates an explosion with a %d unit radius", getAOE() / Tile.TILE_SIZE));
        } else {
			desc.append("The user launches ");
			if(getArcLength() > 0){
				desc.append(
                    String.format(
                        "projectiles in a %d degree arc, each traveling ", 
                        getArcLength()
                    )
                );
			} else {
				desc.append("a projectile, which travels ");
			}
			desc.append(String.format("for %d units, at %d units per second", 
                    getRange() / Tile.TILE_SIZE,
                    getSpeed() * Settings.FPS / Tile.TILE_SIZE
                )
            );
			
			if(getAOE() != 0){
				desc.append(String.format(" before exploding in a %d unit radius", getAOE() / Tile.TILE_SIZE)); 
			}
		}
        
		desc.append(String.format(" dealing %d damage to enemies it hits. \n", getDamage()));
		desc.append(getInflict().getStatusString());
		
        return desc.toString();
	}
    
    public SeedProjectile createProjectile(){
        return new SeedProjectile(nextUseId, getUser().getX(), getUser().getY(), 0, projectileSpeed, getUser(), this);
    }
}
