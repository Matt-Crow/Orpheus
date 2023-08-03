package orpheus.core.utils.timer;

/**
 * something which should be run every frame, and may complete eventually
 */
public interface TimerTask {
    
    /**
     * notifies this time task that a time tick has elapsed
     */
    public void tick();

    /**
     * @return whether this task is done running, and thus should be removed 
     *  from the run queue
     */
    public boolean isDone();
}
