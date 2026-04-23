public class Ball {
    double x = 400, y = 300; 
    int radius = 14; 
    double velocityV = 0, velocityH = 4.0;
    final double jumpForce = -9.5;
    boolean hitRim = false; 

    public void jump() { 
        velocityV = jumpForce; 
    }

    public void wrapAround(int w) {
        if (x > w) x = -radius; 
        else if (x < -radius) x = w;
    }
}
