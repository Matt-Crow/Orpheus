package world.builds.characterClass;

import java.awt.Color;
import java.util.function.BiFunction;
import util.Number;
import world.builds.AbstractBuildAttribute;


/**
 * CharacterClasses are playable characters that every Player plays as.
 * Each character class has its own set of stats and projectile colors.
 * @author Matt
 */
public class CharacterClass extends AbstractBuildAttribute{
    private final Color color;
    private final int maxHP;
    private final double offenseMultiplier;
    private final double defenseMultiplier;
    private final double speed;
    
    private final int baseHp;
    private final int baseDmg;
    private final int baseRed;
    private final int baseSpe;
    
    public static final int BASE_HP = 2000;

    
    public CharacterClass(String name, Color color, int HP, int dmg, int reduction, int speed){
        super(name);
        this.color = color;
        
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
            getColor(), 
            baseHp,
            baseDmg,
            baseRed,
            baseSpe
        );
    }
    
    public int getMaxHP(){
        return maxHP;
    }
    public double getOffMult(){
        return offenseMultiplier;
    }
    public double getDefMult(){
        return defenseMultiplier;
    }
    public double getSpeed(){
        return speed;
    }

    public Color getColor(){
        return color;
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
