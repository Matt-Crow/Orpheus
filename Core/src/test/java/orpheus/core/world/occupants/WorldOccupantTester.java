package orpheus.core.world.occupants;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WorldOccupantTester {
    
    @Test
    public void getCoordinates_returnsReferenceNotCopy() {
        var sut = new WorldOccupantImpl();
        var expected = sut.getCoordinates();
        var actual = sut.getCoordinates();

        Assertions.assertTrue(expected == actual, "references are not equal");
    }
}
