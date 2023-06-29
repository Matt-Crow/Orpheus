package orpheus.core.champions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import world.builds.Build;
import world.builds.DataSet;
import world.builds.actives.AbstractActive;
import world.builds.actives.ElementalActive;
import world.builds.characterClass.CharacterClass;
import world.builds.passives.AbstractPassive;
import world.builds.passives.ThresholdPassive;

public class SpecificationResolverTester {
    
    @Test
    public void givenBuild_whenAnyComponentDoesNotExist_throwsException() {
        var build = new Build("foo", "bar", "a1", "a2", "a3", "p1", "p2", "p3");
        var dataSet = new DataSet();
        var sut = new SpecificationResolver(dataSet);
        
        Assertions.assertThrows(Exception.class, () -> sut.resolve(build));
    }

    @Test
    public void givenBuild_whenAllComponentsExist_doesNotThrowException() {
        var build = new Build("foo", "bar", "a1", "a2", "a3", "p1", "p2", "p3");
        var dataSet = new DataSet();
        dataSet.addCharacterClass(new CharacterClass("bar", null, 0, 0, 0, 0));
        dataSet.addActives(new AbstractActive[] {
            new ElementalActive("a1", 0, 0, 0, 0, 0),
            new ElementalActive("a2", 0, 0, 0, 0, 0),
            new ElementalActive("a3", 0, 0, 0, 0, 0)
        });
        dataSet.addPassives(new AbstractPassive[] {
            new ThresholdPassive("p1", 0),
            new ThresholdPassive("p2", 0),
            new ThresholdPassive("p3", 0)
        });
        var sut = new SpecificationResolver(dataSet);

        var result = sut.resolve(build);

        Assertions.assertEquals(build.getName(), result.getName());
    }

    @Test
    public void givenChampionSpecification_whenNoChampionExists_throwsException() {
        var championSpecification = new ChampionSpecification("foo");
        var dataSet = new DataSet();
        var sut = new SpecificationResolver(dataSet);

        Assertions.assertThrows(Exception.class, () -> sut.resolve(championSpecification));
    }

    @Test
    public void givenChampionSpecification_whenChampionExists_doesNotThrowException() {
        var championSpecification = new ChampionSpecification("foo");
        var dataSet = new DataSet();
        dataSet.addChampion(new Champion(
            "foo", 
            new CharacterClass("bar", null, 0, 0, 0, 0), 
            new AbstractActive[] {

            }, 
            new AbstractPassive[] {
                
            }));
        var sut = new SpecificationResolver(dataSet);

        var result = sut.resolve(championSpecification);

        Assertions.assertEquals(championSpecification.getName(), result.getName());
        Assertions.assertInstanceOf(Champion.class, result);
    }
}
