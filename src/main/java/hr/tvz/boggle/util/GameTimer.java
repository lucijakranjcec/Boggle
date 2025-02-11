package hr.tvz.boggle.util;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer {
    private Timeline timeline;
    private int timeLeft;
    private TimerListener listener;

    public interface TimerListener {
        void onTick(int secondsLeft);
        void onTimeEnd();
    }

    public GameTimer(int initialTime, TimerListener listener) {
        this.timeLeft = initialTime;
        this.listener = listener;
    }

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

    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void reset(int newTime) {
        stop();
        this.timeLeft = newTime;
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
