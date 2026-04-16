public class Hoop {
    double x, y;
    int width = 60;
    int height = 10;
    boolean isOnRight = true;

    public Hoop(int canvasWidth, int canvasHeight) {
        this.y = canvasHeight / 2;
        teleport(canvasWidth);
    }

    public void teleport(int canvasWidth) {
        if (isOnRight) {
            this.x = canvasWidth - 120; // Position on right
        } else {
            this.x = 60; // Position on left
        }
        // Randomize height slightly for variety
        this.y = 150 + (Math.random() * 200);
        isOnRight = !isOnRight; // Toggle side for next time
    }
}
