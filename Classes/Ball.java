public class Ball {
    double x = 150; // Fixed horizontal position
    double y = 300;
    double vy = 0;
    int radius = 18;
    double gravity = 0.45;
    double jumpForce = -9.5;
    boolean touchedGround = false;

    public void jump() {
        vy = jumpForce;
    }

    public void applyPhysics(int groundY) {
        vy += gravity;
        y += vy;

        // Ground Bounce
        if (y + (radius * 2) > groundY) {
            y = groundY - (radius * 2);
            vy *= -0.5;
            touchedGround = true; // Streak resets if you hit the floor
        }
    }
}
