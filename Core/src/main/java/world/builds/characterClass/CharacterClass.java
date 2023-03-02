package world.builds.characterClass;

import gui.graphics.CustomColors;
import java.util.function.BiFunction;
import util.Number;
import world.builds.AbstractBuildAttribute;


/**
 * CharacterClasses are playable characters that every Player plays as.
 * Each character class has its own set of stats and projectile colors.
 * @author Matt
 */
public class CharacterClass extends AbstractBuildAttribute{
    private CustomColors[] colors;
    public static final int BASE_HP = 2000;

    private final int maxHP;
    private final double offenseMultiplier;
    private final double defenseMultiplier;
    private final double speed;
    
    private final int baseHp;
    private final int baseDmg;
    private final int baseRed;
    private final int baseSpe;
    // initializers
    public CharacterClass(String n, CustomColors[] cs, int HP, int dmg, int reduction, int speed){
        super(n);
        colors = cs;
        
        baseHp = Number.minMax(1, HP, 5);
        baseDmg = Number.minMax(1, dmg, 5);
        baseRed = Number.minMax(1, reduction, 5);
        baseSpe = Number.minMax(1, speed, 5);
        
        BiFunction<CharacterStatName, Integer, Double> mult = (CharacterStatName c, Integer base)->{
            return c.getDefaultValue() * (1 + 0.1 * (base - 3));
        };
        maxHP = mult.apply(CharacterStatName.HP, baseHp).intValue();
        offenseMultiplier = mult.apply(CharacterStatName.DMG, baseDmg);
        defenseMultiplier = mult.apply(CharacterStatName.REDUCTION, baseRed);
        this.speed = mult.apply(CharacterStatName.SPEED, baseSpe);
    }
    
    @Override
    public CharacterClass copy(){
        return new CharacterClass(
            getName(), 
            getColors(), 
            getBaseHP(),
            getBaseOffenseMultiplier(),
            getBaseDefenseMultiplier(),
            getBaseSpeed()
        );
    }
    
    public final int getMaxHP(){
        return maxHP;
    }
    public final double getOffMult(){
        return offenseMultiplier;
    }
    public final double getDefMult(){
        return defenseMultiplier;
    }
    public final double getSpeed(){
        return speed;
    }
    
    
    public final int getBaseHP(){
        return baseHp;
    }
    public final int getBaseOffenseMultiplier(){
        return baseDmg;
    }
    public final int getBaseDefenseMultiplier(){
        return baseRed;
    }
    public final int getBaseSpeed(){
        return baseSpe;
    }

    public void setColors(CustomColors[] cs){
        colors = cs;
    }
    public CustomColors[] getColors(){
        return colors;
    }
   
    @Override
    public void init(){
        //dummy init method
    }
    
    @Override
    public void update(){
        
    }
    
    @Override
    public String getDescription(){
        return getName() + ": \n" 
                        + "(*) Maximum hit points: " + getMaxHP() + "\n"
                        + "(*) Damage dealt modifier: " + (int)(getOffMult() * 100) + "%\n"
                        + "(*) Damage taken modifier: " + (int)(getDefMult() * 100) + "%\n"
                        + "(*) Movement speed modifier: " + (int)(getSpeed() * 100) + "%\n";
    }
}
