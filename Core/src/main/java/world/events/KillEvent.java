package world.events;

import orpheus.core.world.occupants.players.Player;

public class KillEvent implements Event {
    
    private final Player killed;

    public KillEvent(Player killed) {
        this.killed = killed;
    }

    public Player getKilled() {
        return killed;
    }
}
