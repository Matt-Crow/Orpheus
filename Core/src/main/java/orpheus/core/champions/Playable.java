package orpheus.core.champions;

import java.util.List;

import orpheus.core.world.graph.Graphable;
import world.builds.actives.AbstractActive;
import world.builds.actives.MeleeActive;
import world.builds.characterClass.CharacterClass;
import world.builds.passives.AbstractPassive;

/**
 * A Playable is something players can play as.
 */
public interface Playable extends Graphable {
    
    public CharacterClass getCharacterClass();

    public MeleeActive getBasicAttack();
    
    public List<AbstractActive> getActives();

    public List<AbstractPassive> getPassives();
}
