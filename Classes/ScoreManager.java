public class ScoreManager {
    private int currentScore = 0;
    private int streak = 0;

    public void scoreBasket(boolean isSwish) {
        if (isSwish) {
            streak++; // Increase streak modifier
            currentScore += (1 + streak); // e.g., 1st swish = +2, 2nd swish = +3
        } else {
            streak = 0; // Reset streak
            currentScore += 1; // Standard point
        }
    }

    public void reset() {
        currentScore = 0;
        streak = 0;
    }

    public int getScore() { return currentScore; }
    public int getStreak() { return streak; }
}
