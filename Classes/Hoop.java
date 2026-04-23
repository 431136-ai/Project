public class Hoop {
    double x, y;
    int height = 100; // The vertical "opening" size
    double speed = 3.8; // How fast the world moves toward you

    public Hoop(int canvasWidth) {
        reset(canvasWidth);
    }

    public void reset(int canvasWidth) {
        this.x = canvasWidth + 50; // Spawns off-screen to the right
        this.y = 100 + (Math.random() * 300); // Random vertical position
    }

    public void move() {
        this.x -= speed; // Moves the hoop toward the left
    }
}
