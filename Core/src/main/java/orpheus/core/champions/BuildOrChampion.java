package orpheus.core.champions;

import java.util.Arrays;

/**
 * Differentiates between Build specifications and Champion specifications.
 */
public enum BuildOrChampion {

    BUILD("build"),
    CHAMPION("champion");

    private final String asString;

    private BuildOrChampion(String asString) {
        this.asString = asString;
    }

    /**
     * Inverse of toString()
     * @param asString the result of BuildOrChampion::toString
     * @throws NoSuchElementException if there exists no enum value for which
     *  toString returns the given input
     * @return a non-null value of this enum.
     */
    public static BuildOrChampion fromString(String asString) {
        var asEnum = Arrays
            .stream(values())
            .filter((e) -> e.asString.equals(asString))
            .findFirst()
            .get();
        return asEnum;
    }

    @Override
    public String toString() {
        return asString;
    }
}
