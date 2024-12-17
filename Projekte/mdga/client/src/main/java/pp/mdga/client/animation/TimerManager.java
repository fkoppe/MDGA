package pp.mdga.client.animation;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TimerManager {
    private final List<TimedTask> tasks = new ArrayList<>();

    /**
     * Add a timed task that will execute after the specified delay.
     *
     * @param delaySeconds The delay in seconds.
     * @param task         The Runnable task to execute after the delay.
     */
    public void addTask(float delaySeconds, Runnable task) {
        tasks.add(new TimedTask(delaySeconds, task));
    }

    /**
     * Update the timer manager to process and execute tasks when their delay has elapsed.
     * This should be called in the `controlUpdate` method or a similar update loop.
     *
     * @param tpf Time per frame (delta time) provided by the update loop.
     */
    public void update(float tpf) {
        Iterator<TimedTask> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            TimedTask task = iterator.next();
            task.update(tpf);
            if (task.isReady()) {
                task.run();
                iterator.remove();
            }
        }
    }

    /**
     * Clears all pending tasks from the manager.
     */
    public void clearTasks() {
        tasks.clear();
    }

    /**
     * Checks if the manager has any pending tasks.
     *
     * @return True if there are pending tasks, otherwise false.
     */
    public boolean hasPendingTasks() {
        return !tasks.isEmpty();
    }

    /**
     * Internal class representing a single timed task.
     */
    private static class TimedTask {
        private float remainingTime;
        private final Runnable task;

        public TimedTask(float delaySeconds, Runnable task) {
            this.remainingTime = delaySeconds;
            this.task = task;
        }

        public void update(float tpf) {
            remainingTime -= tpf;
        }

        public boolean isReady() {
            return remainingTime <= 0;
        }

        public void run() {
            task.run();
        }
    }
}
