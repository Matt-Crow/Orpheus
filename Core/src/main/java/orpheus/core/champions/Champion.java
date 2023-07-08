package orpheus.core.champions;

import java.util.List;
import java.util.Optional;

import orpheus.core.utils.Prototype;
import world.builds.AssembledBuild;
import world.builds.actives.AbstractActive;
import world.builds.actives.MeleeActive;
import world.builds.characterClass.CharacterClass;
import world.builds.passives.AbstractPassive;

/**
 * Someone a user can play as, yet cannot customize.
 */
public abstract class Champion implements Playable, Prototype {
    
    /**
     * Orpheus needs to compose instead of inherit, as 'this' must be passed to
     * some of his ability's constructors.
     */
    private Optional<AssembledBuild> inner = Optional.empty();

    public Champion() {
        inner = Optional.empty();
    }

    public Champion(AssembledBuild inner) {
        setInner(inner);
    }

    protected void setInner(AssembledBuild inner) {
        this.inner = Optional.of(inner);
    }

    protected AssembledBuild getInner() {
        return inner.get();
    }

    @Override
    public CharacterClass getCharacterClass() {
        return inner.get().getCharacterClass();
    }

    @Override
    public MeleeActive getBasicAttack() {
        return inner.get().getBasicAttack();
    }

    @Override
    public List<AbstractActive> getActives() {
        return inner.get().getActives();
    }

    @Override
    public List<AbstractPassive> getPassives() {
        return inner.get().getPassives();
    }

    public ChampionSpecification getSpecification() {
        return new ChampionSpecification(getName());
    }
    
    @Override
    public abstract Champion copy();
}
