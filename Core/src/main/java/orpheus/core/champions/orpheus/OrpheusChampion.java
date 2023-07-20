package orpheus.core.champions.orpheus;

import gui.graphics.CustomColors;
import orpheus.core.champions.Champion;
import world.builds.AssembledBuild;
import world.builds.actives.AbstractActive;
import world.builds.actives.FlameCharge;
import world.builds.actives.HammerToss;
import world.builds.actives.MeleeActive;
import world.builds.actives.SweepingStrike;
import world.builds.characterClass.CharacterClass;
import world.builds.passives.AbstractPassive;
import world.builds.passives.CinderStrikes;
import world.builds.passives.OnBeHitPassive;
import world.statuses.Resistance;
import world.statuses.Strength;

public class OrpheusChampion extends Champion {
    
    /**
     * Scrap metal the player has acquired during the game.
     */
    private int scrapMetal = 0;

    private final ScrapMetal needThisUntilPlayablesUpdate;

    private static final int MAX_SCRAP_METAL = 5;


    public OrpheusChampion() {
        var tanking = new OnBeHitPassive("Tanking", true)
            .withStatus(new Resistance(1, 1))
            .withStatus(new Strength(1, 1));
        needThisUntilPlayablesUpdate = new ScrapMetal(this);
        var passives = new AbstractPassive[] {
            new CinderStrikes(),
            tanking,
            needThisUntilPlayablesUpdate
        };
        var inner = new AssembledBuild(
            getName(), 
            new CharacterClass("Orpheus", CustomColors.GOLD, 3, 3, 4, 2), 
            new SweepingStrike(), 
            makeActives(), 
            passives
        );
        setInner(inner);
    }

    private static AbstractActive[] makeActives() {
        return new AbstractActive[] {
            new HammerToss(),
            new FlameCharge(),
            MeleeActive.makeBasicAttack() // todo replace with Reforge
        };
    }

    /**
     * notifies Orpheus that he has gained another piece of scrap metal
     */
    protected void gainScrapMetal() {
        scrapMetal++;
        if (scrapMetal > MAX_SCRAP_METAL) {
            scrapMetal = MAX_SCRAP_METAL;
        }
    }

    @Override
    public String getName() {
        return "Orpheus";
    }

    @Override
    public OrpheusChampion copy() {
        var result = new OrpheusChampion();
        return result;
    }

    @Override
    public orpheus.core.world.graph.Orpheus toGraph() {
        return new orpheus.core.world.graph.Orpheus(
            scrapMetal,
            needThisUntilPlayablesUpdate.getAngleOffset()
        );
    }
}
