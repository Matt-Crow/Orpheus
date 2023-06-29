package orpheus.core.champions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import world.builds.Build;

public class SpecificationJsonDeserializerTester {

    @Test
    public void givenSerializedBuild_afterDeserializing_toStringsAreEqual() {
        var build = new Build("foo", "bar", "a1", "a2", "a3", "p1", "p2", "p3");
        var sut = new SpecificationJsonDeserializer();

        var result = sut.fromJson(build.toJson());

        Assertions.assertEquals(build.toString(), result.toString());
    }

    @Test
    public void givenSerializedChampion_afterDeserializing_toStringAreEqual() {
        var champion = new ChampionSpecification("foo");
        var sut = new SpecificationJsonDeserializer();

        var result = sut.fromJson(champion.toJson());

        Assertions.assertEquals(champion.toString(), result.toString());
    }
}
