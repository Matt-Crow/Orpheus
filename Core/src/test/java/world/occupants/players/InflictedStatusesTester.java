package world.occupants.players;

import orpheus.core.utils.UndoableOperation;
import orpheus.core.world.occupants.WorldOccupant;
import orpheus.core.world.occupants.players.Player;
import world.Map;
import world.World;
import world.battle.Team;
import world.events.EventListener;
import world.events.OnUpdateEvent;
import world.game.Game;
import world.statuses.AbstractStatus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InflictedStatusesTester {

    @Test
    public void add_givenStatusAlreadyInflicted_clearsTheOld() {
        var world = new WorldMock();
        var player = Player.makeDrone(world, 1);
        var intRef = new IntRef();

        player.inflict(new CounterStatus(intRef, 1));
        player.inflict(new CounterStatus(intRef, 2));
        player.update();

        Assertions.assertEquals(1, intRef.getValue());
    }

    private class WorldMock implements World {

        @Override
        public Map getMap() {
            throw new UnsupportedOperationException("Unimplemented method 'getMap'");
        }

        @Override
        public Game getGame() {
            throw new UnsupportedOperationException("Unimplemented method 'getGame'");
        }

        @Override
        public Team getPlayers() {
            throw new UnsupportedOperationException("Unimplemented method 'getPlayers'");
        }

        @Override
        public Team getAi() {
            throw new UnsupportedOperationException("Unimplemented method 'getAi'");
        }

        @Override
        public void spawn(WorldOccupant e) {
            throw new UnsupportedOperationException("Unimplemented method 'spawn'");
        }

        @Override
        public void init() {
            throw new UnsupportedOperationException("Unimplemented method 'init'");
        }

        @Override
        public void update() {
            throw new UnsupportedOperationException("Unimplemented method 'update'");
        }

        @Override
        public orpheus.core.world.graph.World toGraph() {
            throw new UnsupportedOperationException("Unimplemented method 'toGraph'");
        }

    }

    private class IntRef {
        private int value = 0;

        public int getValue() {
            return value;
        }

        public void inc() {
            value++;
        }
    }

    private class CounterStatus extends AbstractStatus implements EventListener<OnUpdateEvent> {
        private IntRef count;

        public CounterStatus(IntRef count, int intensity) {
            super("test", intensity, 1, x -> x * 100);
            this.count = count;
        }

        @Override
        public UndoableOperation<Player> getInflictor() {
            return makeEventBinder(p -> p.eventOnUpdate(), this);
        }

        @Override
        public String getDesc() {
            return "";
        }

        @Override
        public CounterStatus copy() {
            return new CounterStatus(count, getIntensityLevel());
        }

        @Override
        public void handle(OnUpdateEvent e) {
            count.inc();
        }
    }
}
