public class Ball {
    double x = 400, y = 300; // Start in the center
    int radius = 15;
    double velocityV = 0;
    double velocityH = 4.0; // Base constant speed
    final double jumpForce = -10;
    boolean hitRim = false; 

    public void jump() {
        velocityV = jumpForce;
    }

    public void wrapAround(int canvasWidth) {
        if (x > canvasWidth) x = -radius;
        else if (x < -radius) x = canvasWidth;
    }
}
