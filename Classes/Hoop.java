public class Hoop {
    double x, y;
    int width = 85; // Slightly wider for a better "feel"
    boolean isOnRight = false;

    public Hoop(int canvasWidth) {
        teleport(canvasWidth);
    }

    public void teleport(int canvasWidth) {
        isOnRight = !isOnRight;
        this.y = 180 + (Math.random() * 220);
        if (isOnRight) {
            this.x = (canvasWidth / 2) + 50 + (Math.random() * ((canvasWidth / 2) - 150));
        } else {
            this.x = 50 + (Math.random() * ((canvasWidth / 2) - 150));
        }
    }
}
