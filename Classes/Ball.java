public class Ball {
    double x = 400, y = 300; 
    int radius = 14; 
    double velocityV = 0, velocityH = 4.5; // Starts moving to the right
    final double jumpForce = -9.5;
    boolean hitRim = false; 

    public void jump() { 
        velocityV = jumpForce; 
    }
}
