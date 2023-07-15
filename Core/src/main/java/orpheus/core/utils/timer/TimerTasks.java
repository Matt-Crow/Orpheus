package orpheus.core.utils.timer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * a collection of timer tasks which manages itself
 */
public class TimerTasks {
    
    private ArrayList<TimerTask> tasks = new ArrayList<>();


    public TimerTasks() {
        this(List.of());
    }

    public TimerTasks(Collection<? extends TimerTask> tasks) {
        add(tasks);
    }

    /**
     * adds a timer task which this will manage 
     */
    public void add(TimerTask task) {
        tasks.add(task);
    }

    /**
     * adds many timer tasks which this will manage
     */
    public void add(Collection<? extends TimerTask> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * @return the number of tasks this is managing
     */
    public int size() {
        return tasks.size();
    }

    /**
     * notifies all tasks that a timer tick has elapsed
     */
    public void tick() {
        var incompleteTasks = new ArrayList<TimerTask>();

        for (var task : tasks) {
            if (!task.isDone()) {
                task.tick();
                incompleteTasks.add(task);
            }
        }

        tasks = incompleteTasks;
    }
}
