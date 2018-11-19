package actives;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import java.awt.Color;
import java.awt.Graphics;
import entities.*;
import graphics.CustomColors;
import initializers.Master;
import upgradables.AbstractUpgradable;
import resources.Op;
import resources.Number; // use later for minMax?

public abstract class AbstractActive extends AbstractUpgradable<ActiveStatName>{
    /**
     * Actives are abilities that the user triggers
     */
    private ParticleType particleType;
    private ActiveType type; // used for upcasting
    private int cost; // the energy cost of the active. Calculated automatically
    private ArrayList<Projectile> projectiles;
    private ArrayList<Projectile> nextProjectiles; // projectiles to add at the end of the frame (used to avoid concurrent modification)

    private static int nextUseId = 0; // How many actives have been used thus far. Used to prevent double hitting
    private static HashMap<String, AbstractActive> allActives = new HashMap<>();

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
    }
    public AbstractActive copy(){
            // used to allow override
            // DO NOT INVOKE THIS
            return this;
    }

    // static methods
    public static void addActive(AbstractActive a){
            allActives.put(a.getName().toUpperCase(), a);
    }
    public static void addActives(AbstractActive[] as){
            for(AbstractActive a : as){
                    addActive(a);
            }
    }
    public static AbstractActive getActiveByName(String n){
            AbstractActive ret = allActives.getOrDefault(n.toUpperCase(), allActives.get("SLASH"));
            if(ret.getName().toUpperCase().equals("SLASH") && !n.toUpperCase().equals("SLASH")){
                    Op.add("No active was found with name " + n + " in AbstractActive.getActiveByName");
                    Op.dp();
            }
            return ret;
    }
    public static AbstractActive[] getAll(){
            AbstractActive[] ret = new AbstractActive[allActives.size()];
            Collection<AbstractActive> values = allActives.values();
            int i = 0;
            for(AbstractActive aa : values){
                    ret[i] = aa;
                    i++;
            }
            return ret;
    }
    public static String[] getAllNames(){
            String[] ret = new String[allActives.size()];
            Set<String> keys = allActives.keySet();
            int i = 0;
            for(String key : keys){
                    ret[i] = key;
                    i++;
            }
            return ret;
    }

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
            addStat(new ActiveStat(ActiveStatName.ARC, deg));
            setBase(ActiveStatName.ARC, value);
            break;
        case RANGE:
            // 1-15 units of range. Increases exponentially
            int units = 0;
            value = Number.minMax(0, value, 5);
            for(int i = 0; i <= value; i++){
                    units += i;
            }
            addStat(new ActiveStat(ActiveStatName.RANGE, units * 100));
            setBase(ActiveStatName.RANGE, value);
            break;
        case SPEED:
            // 1-5 units per second
            value = Number.minMax(1, value, 5);
            addStat(new ActiveStat(ActiveStatName.SPEED, 100 * value / Master.FPS));
            setBase(ActiveStatName.SPEED, value);
            break;
        case AOE:
            // 1-5 units (or 0)
            value = Number.minMax(0, value, 5);
            addStat(new ActiveStat(ActiveStatName.AOE, value * 100));
            setBase(ActiveStatName.AOE, value);
            break;
        case DAMAGE:
            // 50-250 to 250-500 damage (will need to balance later?)
            value = Number.minMax(1, value, 5);
            addStat(new ActiveStat(ActiveStatName.DAMAGE, value * 50, 2));
            setBase(ActiveStatName.DAMAGE, value);
            break;
        }
        calculateCost();
    }
    public void calculateCost(){
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

    // misc
    public void init(){
        super.init();
        projectiles = new ArrayList<>();
        nextProjectiles = new ArrayList<>();
    }
    public ActiveType getType(){
        return type;
    }
    public int getCost(){
        return cost;
    }

    // in battle methods
    public boolean canUse(){
        return getRegisteredTo().getEnergyLog().getEnergy() >= cost && !onCooldown();
    }
    public void consumeEnergy(){
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

    public void registerProjectile(Projectile p){
        nextProjectiles.add(p);
    }

    // spawning
    public void spawnProjectile(int facingDegrees){
        registerProjectile(new SeedProjectile(nextUseId, getRegisteredTo().getX(), getRegisteredTo().getY(), facingDegrees, (int) getStatValue(ActiveStatName.SPEED), getRegisteredTo(), this));
    }
    public void spawnProjectile(){
        spawnProjectile(getRegisteredTo().getDir().getDegrees());
    }
    public void spawnArc(int arcDegrees){
        int start = getRegisteredTo().getDir().getDegrees() - arcDegrees / 2;

        // spawn projectiles every 15 degrees
        for(int angleOffset = 0; angleOffset < arcDegrees; angleOffset += 15){
                int angle = start + angleOffset;
                spawnProjectile(angle);
        }
    }

    public void update(){
        super.update();

        projectiles.stream()
        .forEach(p -> p.doUpdate());

        projectiles.stream()
        .filter(p -> !p.getShouldTerminate())
        .forEach(p -> nextProjectiles.add(p));
        projectiles = nextProjectiles;

        nextProjectiles = new ArrayList<>();
    }
    public void drawProjectiles(Graphics g){
        projectiles.stream().forEach(p -> p.draw(g));
    }
    
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
}