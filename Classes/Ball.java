public class Ball {
    double x = 100, y = 300;
    int radius = 15;
    double velocityV = 0;
    double velocityH = 4; // Constant horizontal speed
    final double jumpForce = -10;

    public void jump() {
        velocityV = jumpForce;
    }

    public void updateHorizontal(int canvasWidth) {
        x += velocityH;
        // Bounce off left and right walls
        if (x < 0 || x > canvasWidth - (radius * 2)) {
            velocityH *= -1;
        }
    }
}
