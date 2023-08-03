package world.events;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventListenersTester {
    
    @Test
    public void handle_afterCallingAdd_callsTheNewListener() {
        var sut = new EventListeners<ExampleEvent>();
        var spy = new EventListenerSpy();
        sut.add(spy);

        sut.handle(new ExampleEvent());

        Assertions.assertTrue(spy.hasBeenCalled());
    }
}
