public class Ball {
    double x = 100, y = 300;
    int radius = 15;
    double velocityV = 0;
    double velocityH = 3.5; 
    final double jumpForce = -10;
    
    // Tracks if the ball touched anything before scoring
    boolean hitRimOrBoard = false; 

    public void jump() {
        velocityV = jumpForce;
    }

    public void wrapAround(int canvasWidth) {
        if (x > canvasWidth) x = -radius;
        else if (x < -radius) x = canvasWidth;
    }
}
