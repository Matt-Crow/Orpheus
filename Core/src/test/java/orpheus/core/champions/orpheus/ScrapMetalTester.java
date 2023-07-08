package orpheus.core.champions.orpheus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScrapMetalTester {
    
    @Test
    public void copy_givenOrpheusIsSet_doesNotCloneOrpheus() {
        var sut = new ScrapMetal();
        var orpheus = new OrpheusChampion();

        sut.setOrpheus(orpheus);
        var result = sut.copy();

        Assertions.assertFalse(result.isOrpheusSet());
    }
}
