package orpheus.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.*;

/*
 * run by using
 *  ./gradlew test
 * if any tests fail, it will report with a file I can view in the browser
 * if all tests succeed, produces no output (why?)
 */
public class ExampleTests {

    @Test
    public void doesJUnitWork(){
        Assertions.assertEquals(4, 2 + 2);
    }
}