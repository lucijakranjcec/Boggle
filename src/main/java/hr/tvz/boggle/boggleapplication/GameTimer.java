package hr.tvz.boggle.boggleapplication;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer {
    private Timeline timeline;
    private int timeLeft;
    private TimerListener listener;

    // Listener interface for timer updates
    public interface TimerListener {
        void onTick(int secondsLeft);
        void onTimeEnd();
    }

    // Constructor: set the initial time and listener
    public GameTimer(int initialTime, TimerListener listener) {
        this.timeLeft = initialTime;
        this.listener = listener;
    }

    // Start the timer
    public void start() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (timeLeft > 0) {
                timeLeft--;
                if (listener != null) {
                    listener.onTick(timeLeft);
                }
            } else {
                timeline.stop();
                if (listener != null) {
                    listener.onTimeEnd();
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Stop the timer
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    // Reset the timer to a new time value
    public void reset(int newTime) {
        stop();
        this.timeLeft = newTime;
    }

    // Get the remaining time
    public int getTimeLeft() {
        return timeLeft;
    }
}
