package orpheus.core.utils.timer;

import java.util.function.Consumer;

import javax.swing.Timer;

import orpheus.core.utils.EventEmitter;
import util.Settings;

/**
 * A timer which runs once per frame.
 * The caller is responsible for stopping it once they're done.
 */
public class FrameTimer {
    private final Timer swingTimer;
    private final EventEmitter<EndOfFrameEvent> eventEmitter = new EventEmitter<>();

    public FrameTimer() {
        swingTimer = new Timer(1000 / Settings.FPS, e -> tick());
        swingTimer.stop();
    }

    public FrameTimer(Consumer<EndOfFrameEvent> endOfFrameListener) {
        this();
        addEndOfFrameListener(endOfFrameListener);
    }

    public void start() {
        if (!swingTimer.isRunning()) {
            swingTimer.start();
        }
    }

    public void stop() {
        swingTimer.stop();
    }

    public void addEndOfFrameListener(Consumer<EndOfFrameEvent> endOfFrameListener) {
        eventEmitter.addEventListener(endOfFrameListener);
    }

    private void tick() {
        eventEmitter.emitEvent(new EndOfFrameEvent());
    }
}
