public class Hoop {
    double x, y;
    int height = 80; // The vertical "opening"
    double speed = 3.5; // The screen "scroll" speed

    public Hoop(int canvasWidth) {
        reset(canvasWidth);
    }

    public void reset(int canvasWidth) {
        this.x = canvasWidth + 50; // Spawn off-screen to the right
        this.y = 100 + (Math.random() * 300); // Random vertical position
    }

    public void move() {
        this.x -= speed; // Move toward the player
    }
}
