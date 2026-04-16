public class TimeManager {
    float maxTime = 100;
    float timeLeft;
    float drainRate = 0.2f;

    public TimeManager() { timeLeft = maxTime; }
    public void update() { if (timeLeft > 0) timeLeft -= drainRate; }
    public boolean isTimeUp() { return timeLeft <= 0; }
    public void reset() { timeLeft = maxTime; }
}
