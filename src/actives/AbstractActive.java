package actives;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import graphics.CustomColors;
import javax.json.*;

import controllers.Master;
import upgradables.AbstractUpgradable;
import entities.*;
import serialization.JsonSerialable;
import statuses.*;
import util.Number;

/**
 * The AbstractActive class serves as the base for active abilities possessed by Players
 * @author Matt
 */
public abstract class AbstractActive extends AbstractUpgradable<ActiveStatName> implements JsonSerialable{
    private final ActiveType type; // used for serialization
    private ParticleType particleType; // the type of particles this' projectiles emit @see Projectile
    private int cost; // the energy cost of the active. Calculated automatically
    private final ArrayList<ActiveTag> tags; //tags are used to modify this' behaviour. Only once is currently implemented 
    
    private static int nextUseId = 0; // How many actives have been used thus far. Used to prevent double hitting
    private static final HashMap<String, AbstractActive> ALL_ACTIVES = new HashMap<>();
    static{
        addActive(new ElementalActive(
            "Default",
            3,
            3,
            3,
            3,
            3
        ));
    }

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
     * (dmg * 50) damage.
     */
    public AbstractActive(ActiveType t, String n, int arcLength, int range, int speed, int aoe, int dmg){
        super(n);
        type = t;

        setStat(ActiveStatName.ARC, arcLength);
        setStat(ActiveStatName.RANGE, range);
        setStat(ActiveStatName.SPEED, speed);
        setStat(ActiveStatName.AOE, aoe);
        setStat(ActiveStatName.DAMAGE, dmg);

        particleType = ParticleType.NONE;
        setCooldown(1);

        tags = new ArrayList<>();
    }
    
    public static void loadAll(){
		// read from file later?
		MeleeActive hs = new MeleeActive("Heavy Stroke", 4);
		MeleeActive s = new MeleeActive("Slash", 2);
		
		ElementalActive bt = new ElementalActive("Boulder Toss", 1, 2, 2, 3, 4);
		bt.setParticleType(ParticleType.BURST);
        bt.addTag(ActiveTag.KNOCKSBACK);
		
		ElementalActive cd = new ElementalActive("Cursed Daggers", 2, 5, 5, 0, 0);
		cd.setParticleType(ParticleType.BEAM);
		
		ElementalActive eq = new ElementalActive("Earthquake", 1, 0, 2, 3, 2);
		eq.setParticleType(ParticleType.BURST);
        eq.addTag(ActiveTag.KNOCKSBACK);
		
		ElementalActive fof = new ElementalActive("Fields of Fire", 1, 0, 5, 3, 2);
		fof.setParticleType(ParticleType.SHEAR);
		
		ElementalActive fb = new ElementalActive("Fireball", 2, 3, 3, 3, 4);
		fb.setParticleType(ParticleType.BURST);
		
		ElementalActive mfb = new ElementalActive("Mega Firebolt", 1, 3, 3, 3, 4);
		mfb.setParticleType(ParticleType.SHEAR);
		
		ElementalActive mwb = new ElementalActive("Mini Windbolt", 2, 5, 5, 0, 1);
		mwb.setParticleType(ParticleType.BEAM);
		
		ElementalActive rod = new ElementalActive("RAINBOW OF DOOM", 4, 3, 5, 5, 1);
		rod.setParticleType(ParticleType.BURST);
		
		ElementalActive wb = new ElementalActive("Waterbolt", 1, 3, 3, 1, 2);
		wb.setParticleType(ParticleType.BEAM);
		
		BoostActive ws = new BoostActive("Warrior's Stance", new AbstractStatus[]{new Strength(1, 2), new Resistance(1, 2)});
		BoostActive st = new BoostActive("Speed Test", new AbstractStatus[]{new Rush(2, 3)});
		BoostActive ss = new BoostActive("Shield Stance", new AbstractStatus[]{new Resistance(2, 3)});
		BoostActive hr = new BoostActive("Healing Rain", new AbstractStatus[]{new Regeneration(2, 3)});
		BoostActive bs = new BoostActive("Blade Stance", new AbstractStatus[]{new Strength(2, 3)});
		
		addActives(new AbstractActive[]{
			s,
			hs,
			bt,
			cd,
			eq,
			fof,
			fb,
			mfb,
			mwb,
			rod,
			wb,
			ws,
			st,
			ss,
			hr,
			bs
		});
	}
    
    // static methods
    public static void addActive(AbstractActive a){
        ALL_ACTIVES.put(a.getName().toUpperCase(), a.copy());
    }
    public static void addActives(AbstractActive[] as){
        for(AbstractActive a : as){
            addActive(a);
        }
    }
    
    /**
     * Finds an active with the given name, ignoring case.
     * If no active is found, throws a NoSuchElementException
     * @param n the name of the active to search for
     * @return the active with that name
     */
    public static AbstractActive getActiveByName(String n){
        if(!ALL_ACTIVES.containsKey(n.toUpperCase())){
            throw new NoSuchElementException(n + " is not in the list of actives. Did you remember to call AbstractActive.addActive(...);?");
        }
        return ALL_ACTIVES.get(n.toUpperCase()).copy();
    }
    public static AbstractActive[] getAll(){
        AbstractActive[] ret = new AbstractActive[ALL_ACTIVES.size()];
        Collection<AbstractActive> values = ALL_ACTIVES.values();
        int i = 0;
        for(AbstractActive aa : values){
            ret[i] = aa;
            i++;
        }
        return ret;
    }
    public static String[] getAllNames(){
        String[] ret = new String[ALL_ACTIVES.size()];
        Set<String> keys = ALL_ACTIVES.keySet();
        int i = 0;
        for(String key : keys){
            ret[i] = key;
            i++;
        }
        return ret;
    }
    
    public final ActiveType getType(){
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
            // 50-250 to 250-500 damage (will need to balance later?)
            value = Number.minMax(1, value, 5);
            addStat(ActiveStatName.DAMAGE, value, value * 50);
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
    
    
    
    

    // in battle methods
    public final boolean canUse(){
        return getRegisteredTo().getEnergyLog().getEnergy() >= cost && !onCooldown();
    }

    /**
     * Creates a projectile at this' user's coordinates
     * @param facingDegrees the direction the new projectile will travel
     */
    private void spawnProjectile(int facingDegrees){
        getRegisteredTo().spawn(new SeedProjectile(nextUseId, getRegisteredTo().getX(), getRegisteredTo().getY(), facingDegrees, (int) getStatValue(ActiveStatName.SPEED), getRegisteredTo(), this));    
    }
    
    /**
     * Generates an arc of projectiles from this' user
     * @param arcDegrees the number of degrees in the arc
     */
    private void spawnArc(int arcDegrees){
        int start = getRegisteredTo().getDir().getDegrees() - arcDegrees / 2;
        // spawn projectiles every 15 degrees
        for(int angleOffset = 0; angleOffset < arcDegrees; angleOffset += 15){
            spawnProjectile(start + angleOffset);
        }
    }
    
    private void consumeEnergy(){
        getRegisteredTo().getEnergyLog().loseEnergy(cost);
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
        g.drawString("Energy cost: " + getRegisteredTo().getEnergyLog().getEnergy() + "/" + cost, x + 10, y + 33);
    }
    
    @Override
    public abstract AbstractActive copy();
    
    @Override
    public JsonObject serializeJson(){
        JsonObject obj = super.serializeJson();
        //javax jsonObjects are immutable
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        b.add("type", type.toString());
        b.add("particle type", particleType.toString());
        JsonArrayBuilder a = Json.createArrayBuilder();
        tags.forEach((t) -> {
            a.add(t.toString());
        });
        b.add("tags", a.build());
        return b.build();
    }
    
    public static AbstractActive deserializeJson(JsonObject obj){
        if(!obj.containsKey("type")){
            throw new JsonException("JsonObject missing key 'type'");
        }
        AbstractActive ret = null;
        switch(ActiveType.fromString(getTypeFrom(obj))){
            case MELEE:
                ret = MeleeActive.deserializeJson(obj);
                break;
            case BOOST:
                ret = BoostActive.deserializeJson(obj);
                break;
            case ELEMENTAL:
                ret = ElementalActive.deserializeJson(obj);
                break;
            default:
                System.out.println("Abstract active cannot deserialize " + obj.getString("type"));
                break;
        }
        return ret;
    }
    public static ParticleType getParticleTypeFrom(JsonObject obj){
        if(!obj.containsKey("particle type")){
            throw new JsonException("Json Object is missing key 'particle type'");
        }
        return ParticleType.fromString(obj.getString("particle type"));
    }
    public static ArrayList<ActiveTag> getTagsFrom(JsonObject obj){
        if(!obj.containsKey("tags")){
            throw new JsonException("Json Object missing key 'tags'");
        }
        ArrayList<ActiveTag> ret = new ArrayList<>();
        ActiveTag tag = null;
        for(JsonValue jv : obj.getJsonArray("tags")){
            if(jv.getValueType().equals(JsonValue.ValueType.STRING)){
                tag = ActiveTag.fromString(((JsonString)jv).getString());
                if(tag == null){
                    throw new NullPointerException("Unknown tag: " + jv);
                } else {
                    ret.add(tag);
                }
            }
        }
        return ret;
    }
}