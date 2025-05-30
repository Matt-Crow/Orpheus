package world.events;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventListenersTester {
    
    @Test
    public void handle_afterCallingAdd_callsTheNewListener() {
        var sut = new EventListeners<Object>();
        var spy = new EventListenerSpy();
        sut.add(spy);

        sut.handle(new Object());

        Assertions.assertTrue(spy.hasBeenCalled());
    }

    @Test
    public void remove_doesNotHaveConcurrentModification() {
        var sut = new EventListeners<Object>();
        var spy = new EventListenerSpy();
        sut.add(spy);
        sut.add(e -> sut.remove(spy));

        sut.handle(new Object());
    }
}
