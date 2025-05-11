package world.events;

public class EventListenerSpy implements EventListener<Object> {

    private boolean called = false;

    public boolean hasBeenCalled() {
        return called;
    }

    @Override
    public void handle(Object e) {
        called = true;
    }
}
