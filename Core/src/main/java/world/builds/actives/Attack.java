package world.builds.actives;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import orpheus.core.world.occupants.players.Player;
import world.builds.characterClass.CharacterClass;
import world.builds.characterClass.CharacterStatName;
import world.statuses.AbstractStatus;

/**
 * an attack represents a usage of an active ability which either deals damage
 * or inflicts statuses
 */
public class Attack {
    
    /**
     * The base damage this will inflict on targets, except without factoring
     * in their damage reduction.
     */
    private final double baseDamage;

    /**
     * The statuses this will inflict upon resolving against a player.
     */
    private final Collection<AbstractStatus> statuses;

    /**
     * The players who have been struck by this attack
     */
    private final HashSet<Player> resolvedAgainst = new HashSet<>();


    public Attack(Damage damage) {
        this(damage.getPercentage() * CharacterClass.BASE_HP, Set.of());
    }

    public Attack(double baseDamage, Collection<AbstractStatus> statuses) {
        this.baseDamage = baseDamage;
        this.statuses = statuses;
    }


    /**
     * Resolves this attack against the given player, if this attack has not
     * yet been resolved against them.
     * @param player the player to resolve this attack against.
     */
    public void resolveAgainst(Player player) {
        if (!resolvedAgainst.contains(player)) {
            doResolveAgainst(player);
        }
    }

    private void doResolveAgainst(Player player) {
        var damage = baseDamage / player.getStatValue(CharacterStatName.REDUCTION);
        player.takeDamage((int)damage);
        statuses.forEach(player::inflict);
        resolvedAgainst.add(player);
    }
}
