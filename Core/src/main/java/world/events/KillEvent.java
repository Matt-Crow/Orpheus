package world.events;

import world.entities.AbstractPlayer;

public class KillEvent implements Event {
    
    private final AbstractPlayer killed;

    public KillEvent(AbstractPlayer killed) {
        this.killed = killed;
    }

    public AbstractPlayer getKilled() {
        return killed;
    }
}
