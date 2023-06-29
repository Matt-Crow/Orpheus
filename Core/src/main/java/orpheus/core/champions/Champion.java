package orpheus.core.champions;

import java.util.Arrays;

import world.builds.AssembledBuild;
import world.builds.actives.AbstractActive;
import world.builds.characterClass.CharacterClass;
import world.builds.passives.AbstractPassive;

/**
 * Someone a user can play as, yet cannot customize.
 */
public class Champion extends AssembledBuild {
    
    public Champion(
            String name, 
            CharacterClass characterClass, 
            AbstractActive[] actives,
            AbstractPassive[] passives
    ) {
        super(name, characterClass, actives, passives);
    }

    public Champion copy() {
        return new Champion(
            getName(), 
            getCharacterClass().copy(), 
            copy(getActives()), 
            copy(getPassives())
        );
    }

    private AbstractActive[] copy(AbstractActive[] original) {
        return Arrays.stream(original)
            .map((a) -> a.copy())
            .toArray(AbstractActive[]::new);
    }

    private AbstractPassive[] copy(AbstractPassive[] original) {
        return Arrays.stream(original)
            .map((a) -> a.copy())
            .toArray(AbstractPassive[]::new);
    }
}
