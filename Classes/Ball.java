public class Ball {
    double x = 100;
    double y = 300;
    int radius = 15;
    double velocityV = 0;
    final double jumpForce = -10;

    public void jump() {
        velocityV = jumpForce;
    }
}
