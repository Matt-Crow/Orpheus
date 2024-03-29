package world.builds;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import orpheus.core.champions.Playable;
import world.builds.actives.AbstractActive;
import world.builds.actives.MeleeActive;
import world.builds.characterClass.CharacterClass;
import world.builds.passives.AbstractPassive;

/**
 * an AssembledBuild is a Build which has been loaded from a DataSet
 */
public class AssembledBuild implements Playable {
    private final String name;
    private final CharacterClass characterClass;
    private final AbstractActive[] actives;
    private final AbstractPassive[] passives;
    private final MeleeActive basicAttack;

    public AssembledBuild(
        String name,
        CharacterClass characterClass,
        AbstractActive[] actives,
        AbstractPassive[] passives
    ) {
        this(name, characterClass, MeleeActive.makeBasicAttack(), actives, passives);
    }

    public AssembledBuild(
        String name,
        CharacterClass characterClass,
        MeleeActive basicAttack,
        AbstractActive[] actives,
        AbstractPassive[] passives
    ) {
        if (characterClass == null) {
            throw new IllegalArgumentException("characterClass must not be null");
        }
        if (basicAttack == null) {
            throw new IllegalArgumentException("basicAttack must not be null");
        }
        if (Arrays.stream(actives).anyMatch((act) -> act == null)) {
            throw new IllegalArgumentException("cannot have null active");
        }
        if (Arrays.stream(passives).anyMatch((pas) -> pas == null)) {
            throw new IllegalArgumentException("cannot have null passive");
        }
        this.name = name;
        this.characterClass = characterClass;
        this.basicAttack = basicAttack;
        this.actives = actives;
        this.passives = passives;
    }

    public String getName() {
        return name;
    }

    @Override
    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    
    /**
     * @return the default attack the player will use
     */
    @Override
    public MeleeActive getBasicAttack() {
        return basicAttack;
    }

    @Override
    public List<AbstractActive> getActives() {
        return List.of(actives);
    }

    @Override
    public List<AbstractPassive> getPassives() {
        return List.of(passives);
    }

    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        String sep = "~~~~~~~~~~~~~~~~~~~~";
        sb.append("Build ").append(name).append(": \n");
        sb.append("Class: \n").append(entab(characterClass.getDescription()))
            .append("\n");
        sb.append("Actives: \n");
        for(AbstractActive an : actives){
            sb.append(
                entab(
                    sep + '\n' +
                    an.getName() + ": " + an.getDescription()
                )
            ).append("\n");
        }
        sb.append("Passives: \n");
        for(AbstractPassive pn : passives){
            sb.append(
                entab(
                    sep + '\n' +
                    pn.getName() + ": " + pn.getDescription()
                )
            ).append("\n");
        }
        return sb.toString();
    }

    private static String entab(String s){
        return Arrays.stream(s.split("\n")).collect(Collectors.joining("\n\t", "\t", ""));
    }

    @Override
    public orpheus.core.world.graph.playables.Playable toGraph() {
        return new orpheus.core.world.graph.playables.AssembledBuild();
    }
}
