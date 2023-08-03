package orpheus.core.utils.timer;

/**
 * a task which can be done a given number of times
 */
public class DoXTimes implements TimerTask {

    private final Runnable doThis;
    private final int times;
    private int timesDone = 0;

    public DoXTimes(Runnable doThis, int times) {
        this.doThis = doThis;
        this.times = times;
    }

    /**
     * @param times the number of times to do nothing
     * @return a timer task which does nothing
     */
    public static DoXTimes nothing(int times) {
        return new DoXTimes(() -> {}, times);
    }

    @Override
    public void tick() {
        doThis.run();
        ++timesDone;
    }

    @Override
    public boolean isDone() {
        return times == timesDone;
    }
}
