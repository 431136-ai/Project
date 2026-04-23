public class ScoreManager {
    private int score = 0, streak = 0;

    public void scoreBasket(boolean swish) {
        if (swish) { 
            streak++; 
            score += (1 + streak); 
        } else { 
            streak = 0; 
            score += 1; 
        }
    }

    public void reset() { score = 0; streak = 0; }
    public int getScore() { return score; }
    public int getStreak() { return streak; }
}
