package world.events;

public class EventListenerSpy implements EventListener<ExampleEvent> {

    private boolean called = false;

    public boolean hasBeenCalled() {
        return called;
    }

    @Override
    public void handle(ExampleEvent e) {
        called = true;
    }
}
