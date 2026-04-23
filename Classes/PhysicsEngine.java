public class PhysicsEngine {
    double gravity = 0.45;

    // Fixed to match the call in TapTapShoot: applyPhysics(ball)
    public void applyPhysics(Ball ball) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;
    }

    // New logic for passing through a vertical gate
    public boolean checkPassThrough(Ball b, Hoop h) {
        // Check if ball's X has reached the Hoop's X line
        if (b.x + b.radius > h.x && b.x - b.radius < h.x + 10) {
            // Check if ball's Y is inside the gap
            if (b.y > h.y && b.y < h.y + h.height) {
                return true;
            }
        }
        return false;
    }
}
