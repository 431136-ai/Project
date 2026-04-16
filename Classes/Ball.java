public class Ball {
    double x = 100, y = 300;
    int radius = 15;
    double velocityV = 0;
    double velocityH = 3; // Constant horizontal speed
    final double jumpForce = -10;

    public void jump() {
        velocityV = jumpForce;
    }

    public void wrapAround(int canvasWidth) {
        if (x > canvasWidth) {
            x = -radius; // Appear on left
        } else if (x < -radius) {
            x = canvasWidth; // Appear on right
        }
    }
}
