package orpheus.core.champions.orpheus;

import java.awt.Color;
import gui.graphics.CustomColors;
import orpheus.core.champions.Champion;
import world.builds.actives.AbstractActive;
import world.builds.actives.FlameCharge;
import world.builds.actives.HammerToss;
import world.builds.actives.MeleeActive;
import world.builds.actives.SweepingStrike;
import world.builds.characterClass.CharacterClass;
import world.builds.passives.AbstractPassive;
import world.builds.passives.CinderStrikes;
import world.builds.passives.OnBeHitPassive;
import world.builds.passives.ThresholdPassive;
import world.statuses.Resistance;
import world.statuses.Rush;
import world.statuses.Strength;

public class OrpheusChampion extends Champion {
    
    public OrpheusChampion() {
        super(
            "Orpheus", 
            makeCharacterClass(), 
            new SweepingStrike(), 
            makeActives(), 
            makePassives()
        );
    }

    private static CharacterClass makeCharacterClass() {
        return new CharacterClass(
            "Orpheus", 
            CustomColors.METAL.toArray(Color[]::new), 
            3, 
            3, 
            4, 
            2
        );
    }

    private static AbstractActive[] makeActives() {
        return new AbstractActive[] {
            new HammerToss(),
            new FlameCharge(),
            MeleeActive.makeBasicAttack() // todo replace with Reforge
        };
    }

    private static AbstractPassive[] makePassives() {
        var tanking = new OnBeHitPassive("Tanking", true)
            .withStatus(new Resistance(1, 1))
            .withStatus(new Strength(1, 1));
        return new AbstractPassive[] {
            new CinderStrikes(),
            tanking,
            new ThresholdPassive("Temp", 1).withStatus(new Rush(3, 1))// todo replace with Detritus
        };
    }

    @Override
    public OrpheusChampion copy() {
        return new OrpheusChampion();
    }
}
