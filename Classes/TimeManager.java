public class TimeManager {
    double timeLeft;
    double maxTime = 100.0; // Starting time in "units"

    public TimeManager() {
        reset();
    }

    public void reset() {
        timeLeft = maxTime;
    }

    public void update() {
        if (timeLeft > 0) {
            timeLeft -= 0.25; // Speed of the countdown
        }
    }

    public boolean isTimeUp() {
        return timeLeft <= 0;
    }
}
