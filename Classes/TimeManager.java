public class TimeManager {
    float maxTime = 100; // 100% of the bar
    float timeLeft;
    float drainRate = 0.2f; // How fast time disappears

    public TimeManager() {
        timeLeft = maxTime;
    }

    public void update() {
        if (timeLeft > 0) timeLeft -= drainRate;
    }

    public void addBonus(float amount) {
        timeLeft = Math.min(maxTime, timeLeft + amount);
    }

    public boolean isTimeUp() {
        return timeLeft <= 0;
    }

    public void reset() {
        timeLeft = maxTime;
    }
}
