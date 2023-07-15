package orpheus.core.utils.timer;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimerTasksTester {
    
    @Test
    public void tick_givenDoneTask_doesNotCallTickOnIt() {
        var doneTask = new DoneTimerTaskSpy();
        var sut = new TimerTasks();
        sut.add(doneTask);

        sut.tick();

        Assertions.assertFalse(doneTask.hasBeenCalled());
    }

    @Test
    public void tick_givenNotDoneTask_callsTickOnIt() {
        var notDoneTask = new NotDoneTimerTaskSpy();
        var sut = new TimerTasks();
        sut.add(notDoneTask);

        sut.tick();

        Assertions.assertTrue(notDoneTask.hasBeenCalled());
    }

    @Test
    public void tick_given3Tasks_removesCompletedOnesAfterTick() {
        var tasks = List.of(
            new DoneTimerTaskSpy(), 
            new NotDoneTimerTaskSpy(),
            new NotDoneTimerTaskSpy()
        );
        var sut = new TimerTasks();
        sut.add(tasks);

        sut.tick();

        Assertions.assertEquals(2, sut.size());
    }

    @Test
    public void tick_givenDoXTimes_runsThemCorrectly() {
        var tasks = List.of(
            DoXTimes.nothing(1),
            DoXTimes.nothing(2),
            DoXTimes.nothing(3)
        );
        var sut = new TimerTasks();
        sut.add(tasks);

        sut.tick();
        Assertions.assertEquals(3, sut.size());
        sut.tick();
        Assertions.assertEquals(2, sut.size());
        sut.tick();
        Assertions.assertEquals(1, sut.size());
        sut.tick();
        Assertions.assertEquals(0, sut.size());
    }

    private class DoneTimerTaskSpy implements TimerTask {

        private boolean called = false;

        public boolean hasBeenCalled() {
            return called;
        }

        @Override
        public void tick() {
            called = true;
        }

        @Override
        public boolean isDone() {
            return true;
        }
    }

    private class NotDoneTimerTaskSpy implements TimerTask {

        private boolean called = false;

        public boolean hasBeenCalled() {
            return called;
        }

        @Override
        public void tick() {
            called = true;
        }

        @Override
        public boolean isDone() {
            return false;
        }

    }
}
