public class PhysicsEngine {
    double gravity = 0.45;
    double friction = 0.65; 

    public void applyPhysics(Ball ball, int w, Hoop hoop) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;

        // Keep horizontal direction locked toward the current hoop
        if (hoop.isOnRight && ball.velocityH < 0) ball.velocityH *= -1;
        if (!hoop.isOnRight && ball.velocityH > 0) ball.velocityH *= -1;
        
        ball.x += ball.velocityH;
        ball.wrapAround(w);

        // Check for collisions with the two tips of the hoop
        resolveRimBounce(ball, hoop.x, hoop.y);
        resolveRimBounce(ball, hoop.x + hoop.width, hoop.y);
    }

    private void resolveRimBounce(Ball ball, double rx, double ry) {
        double bx = ball.x + ball.radius;
        double by = ball.y + ball.radius;
        double dx = bx - rx, dy = by - ry;
        double dist = Math.sqrt(dx*dx + dy*dy);

        if (dist < ball.radius) {
            ball.hitRim = true;
            double nx = dx/dist, ny = dy/dist;
            double dot = ball.velocityH * nx + ball.velocityV * ny;
            ball.velocityH = (ball.velocityH - 2 * dot * nx) * friction;
            ball.velocityV = (ball.velocityV - 2 * dot * ny) * friction;
            // Positional correction to prevent sticking
            ball.x = rx + nx * ball.radius - ball.radius;
            ball.y = ry + ny * ball.radius - ball.radius;
        }
    }

    public boolean checkScore(Ball b, Hoop h) {
        // Center of ball must pass through the top of the hoop while falling
        return b.x + b.radius > h.x && b.x + b.radius < h.x + h.width &&
               b.y + b.radius > h.y && b.y + b.radius < h.y + 15 && b.velocityV > 0;
    }
}+
