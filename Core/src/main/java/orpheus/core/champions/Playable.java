package orpheus.core.champions;

import java.util.List;

import orpheus.core.world.graph.Graphable;
import orpheus.core.world.occupants.players.Player;
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

    public default void setUser(Player player) {
        getCharacterClass().setUser(player);
        getBasicAttack().setUser(player);
        getActives().forEach(a -> a.setUser(player));
        getPassives().forEach(p -> p.setUser(player));
    }

    public default void init() {
        getCharacterClass().init();
        getBasicAttack().init();
        getActives().forEach(AbstractActive::init);
        getPassives().forEach(AbstractPassive::init);
    }

    public default void update() {
        getCharacterClass().update();
        getBasicAttack().update();
        getActives().forEach(AbstractActive::update);
        getPassives().forEach(AbstractPassive::update);
    }

    @Override
    public orpheus.core.world.graph.playables.Playable toGraph();
}
