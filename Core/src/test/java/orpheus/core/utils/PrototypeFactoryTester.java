package orpheus.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrototypeFactoryTester {
    
    @Test
    public void make_givenUnregisteredName_throwsException() {
        var sut = new PrototypeFactory<Prototype>();

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.make("foo"));
    }

    @Test
    public void make_givenRegisteredName_returnsCopyOfRegistered() {
        var registered = new ExamplePrototype("foo");
        var sut = new PrototypeFactory<ExamplePrototype>();
        sut.add(registered);

        var result = sut.make(registered.getName());

        Assertions.assertEquals(registered.getName(), result.getName());
    }

    private class ExamplePrototype implements Prototype {

        private final String name;

        public ExamplePrototype(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public ExamplePrototype copy() {
            return new ExamplePrototype(name);
        }
    }
}
