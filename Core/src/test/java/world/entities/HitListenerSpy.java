package world.entities;

import world.events.EventListener;
import world.events.OnHitEvent;

public class HitListenerSpy implements EventListener<OnHitEvent> {

    private int timesCalled = 0;

    public int getTimesCalled() {
        return timesCalled;
    }

    @Override
    public void handle(OnHitEvent e) {
        timesCalled++;
    }
}
