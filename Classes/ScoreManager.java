public class ScoreManager {
    private int currentScore = 0;

    public void increment() {
        currentScore++;
    }

    public void reset() {
        currentScore = 0;
    }

    public int getScore() {
        return currentScore;
    }
}
