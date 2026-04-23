public class TimeManager {
    float timeLeft = 100, drainRate = 0.22f;

    public void update() { 
        if (timeLeft > 0) timeLeft -= drainRate; 
    }
    
    public boolean isTimeUp() { return timeLeft <= 0; }
    public void reset() { timeLeft = 100; }
}
