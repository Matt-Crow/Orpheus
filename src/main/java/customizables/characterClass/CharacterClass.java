package customizables.characterClass;

import customizables.AbstractCustomizable;
import graphics.CustomColors;
import customizables.CustomizableType;
import java.text.DecimalFormat;
import java.util.function.BiFunction;
import util.Number;


/**
 * CharacterClasses are playable characters that every Player plays as.
 * Each character class has its own set of stats and projectile colors.
 * @author Matt
 */
public class CharacterClass extends AbstractCustomizable{
    private CustomColors[] colors;
    public static final int BASE_HP = 2000;

    private final int maxHP;
    private final int maxEnergy;
    private final double offenseMultiplier;
    private final double defenseMultiplier;
    private final int speed;
    
    private final int baseHp;
    private final int baseEne;
    private final int baseDmg;
    private final int baseRed;
    private final int baseSpe;
    // initializers
    public CharacterClass(String n, CustomColors[] cs, int HP, int energy, int dmg, int reduction, int speed){
        super(n);
        colors = cs;
        
        baseHp = Number.minMax(1, HP, 5);
        baseEne = Number.minMax(1, energy, 5);
        baseDmg = Number.minMax(1, dmg, 5);
        baseRed = Number.minMax(1, reduction, 5);
        baseSpe = Number.minMax(1, speed, 5);
        
        BiFunction<CharacterStatName, Integer, Double> mult = (CharacterStatName c, Integer base)->{
            return c.getDefaultValue() * (1 + 0.1 * (base - 3));
        };
        maxHP = mult.apply(CharacterStatName.HP, baseHp).intValue();
        maxEnergy = mult.apply(CharacterStatName.ENERGY, baseEne).intValue();
        offenseMultiplier = mult.apply(CharacterStatName.DMG, baseDmg);
        defenseMultiplier = mult.apply(CharacterStatName.REDUCTION, baseRed);
        this.speed = mult.apply(CharacterStatName.SPEED, baseSpe).intValue();
    }
    
    @Override
    public CharacterClass copy(){
        return new CharacterClass(
            getName(), 
            getColors(), 
            getBaseHP(),
            getBaseEnergy(),
            getBaseOffenseMultiplier(),
            getBaseDefenseMultiplier(),
            getBaseSpeed()
        );
    }
    
    public final int getMaxHP(){
        return maxHP;
    }
    public final int getMaxEnergy(){
        return maxEnergy;
    }
    public final double getOffMult(){
        return offenseMultiplier;
    }
    public final double getDefMult(){
        return defenseMultiplier;
    }
    public final int getSpeed(){
        return speed;
    }
    
    
    public final int getBaseHP(){
        return baseHp;
    }
    public final int getBaseEnergy(){
        return baseEne;
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
    public String getDescription(){
        return getName() + ": \n" 
                        + "(*) Maximum hit points: " + getMaxHP() + "\n"
                        + "(*) Maximum energy: " + new DecimalFormat("##.#").format(getMaxEnergy()) + "\n"
                        + "(*) Damage dealt modifier: " + (int)(getOffMult() * 100) + "%\n"
                        + "(*) Damage taken modifier: " + (int)(getDefMult() * 100) + "%\n"
                        + "(*) Movement speed modifier: " + (int)(getSpeed() * 100) + "%\n";
    }
}
