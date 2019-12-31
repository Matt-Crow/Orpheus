package customizables.characterClass;

import customizables.AbstractCustomizable;
import graphics.CustomColors;
import customizables.CustomizableType;
import java.text.DecimalFormat;
import util.Number;


/**
 * CharacterClasses are playable characters that every Player plays as.
 * Each character class has its own set of stats and projectile colors.
 * @author Matt
 */
public class CharacterClass extends AbstractCustomizable{
    private CustomColors[] colors;
    public static final int BASE_HP = 2000;

    // initializers
    public CharacterClass(String n, CustomColors[] cs, int HP, int energy, int dmg, int reduction, int speed){
        super(CustomizableType.CHARACTER_CLASS, n);
        colors = cs;

        setStat(CharacterStatName.HP, HP);
        setStat(CharacterStatName.ENERGY, energy);
        setStat(CharacterStatName.DMG, dmg);
        setStat(CharacterStatName.REDUCTION, reduction);
        setStat(CharacterStatName.SPEED, speed);
    }
    
    @Override
    public CharacterClass copy(){
        return new CharacterClass(
            getName(), 
            getColors(), 
            getBase(CharacterStatName.HP),
            getBase(CharacterStatName.ENERGY),
            getBase(CharacterStatName.DMG),
            getBase(CharacterStatName.REDUCTION),
            getBase(CharacterStatName.SPEED)
        );
    }

    public void setColors(CustomColors[] cs){
        colors = cs;
    }
    public CustomColors[] getColors(){
        return colors;
    }
    public void setStat(CharacterStatName c, int value){
        value = Number.minMax(1, value, 5);
        
        /*
        value | multiplier
        ------------------
        1     | 80%
        2     | 90%
        3     | 100%
        4     | 110%
        5     | 120%
        */
        addStat(c, value, c.getDefaultValue() * (1 + 0.1 * (value - 3)));
    }
    @Override
    public String getDescription(){
        return getName() + ": \n" 
                        + "(*) Maximum hit points: " + (int)getStatValue(CharacterStatName.HP) + "\n"
                        + "(*) Maximum energy: " + new DecimalFormat("##.#").format(getStatValue(CharacterStatName.ENERGY)) + "\n"
                        + "(*) Damage dealt modifier: " + (int)(getStatValue(CharacterStatName.DMG) * 100) + "%\n"
                        + "(*) Damage taken modifier: " + (int)(getStatValue(CharacterStatName.REDUCTION) * 100) + "%\n"
                        + "(*) Movement speed modifier: " + (int)(getStatValue(CharacterStatName.SPEED) * 100) + "%\n";
    }
}
