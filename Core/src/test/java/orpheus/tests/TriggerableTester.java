package orpheus.tests;

import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import util.SafeList;
import world.events.Triggerable;

public class TriggerableTester {
    
    @Test
    public void testTriggerable(){
        Consumer<Integer> doNothing = (i)->{};
        SafeList<Triggerable<Integer>> triggerables = new SafeList<>();
        triggerables.add(new Triggerable<>(3, doNothing));
        triggerables.add(new Triggerable<>(1, doNothing));

        triggerables.forEach((t)->t.trigger(0));
        Assertions.assertEquals(1, triggerables.length());

        triggerables.forEach((t)->t.trigger(0));
        triggerables.forEach((t)->t.trigger(0));
        Assertions.assertEquals(0, triggerables.length());
    }
}
