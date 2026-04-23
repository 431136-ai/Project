public class Ball {
    double x = 250, y = 300; // Anchored to the left side
    int radius = 14;
    double velocityV = 0, velocityH = 0;
    final double jumpForce = -9.5;
    boolean hitRim = false; 

    public void jump() { 
        velocityV = jumpForce; 
    }
}
