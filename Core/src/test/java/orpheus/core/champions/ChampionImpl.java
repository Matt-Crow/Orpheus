package orpheus.core.champions;

import orpheus.core.world.graph.GraphElement;
import world.builds.AssembledBuild;
import world.builds.actives.AbstractActive;
import world.builds.actives.MeleeActive;
import world.builds.characterClass.CharacterClass;
import world.builds.passives.AbstractPassive;

public class ChampionImpl extends Champion {

    public ChampionImpl() {
        super(new AssembledBuild(
            "foo", 
            new CharacterClass("bar", null, 0, 0, 0, 0), 
            MeleeActive.makeBasicAttack(),
            new AbstractActive[] {}, 
            new AbstractPassive[] {}
        ));
    }

    @Override
    public String getName() {
        return getInner().getName();
    }

    @Override
    public Champion copy() {
        return new ChampionImpl();
    }

    @Override
    public GraphElement toGraph() {
        return null;
    }
    
}
