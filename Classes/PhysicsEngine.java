public class PhysicsEngine {
    double gravity = 0.45;
    double friction = 0.65; 
    double moveSpeed = 4.5;

    public void applyPhysics(Ball ball, int canvasWidth, int canvasHeight, Hoop hoop) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;
        ball.velocityH = hoop.isOnRight ? moveSpeed : -moveSpeed;
        ball.x += ball.velocityH;

        // Screen Wrap
        if (ball.x > canvasWidth) ball.x = -ball.radius * 2;
        else if (ball.x + ball.radius * 2 < 0) ball.x = canvasWidth;

        // Floor/Ceiling
        if (ball.y < 0) { ball.y = 0; ball.velocityV = Math.abs(ball.velocityV) * 0.3; }
        if (ball.y + ball.radius * 2 > canvasHeight) {
            ball.y = canvasHeight - ball.radius * 2;
            ball.velocityV = -Math.abs(ball.velocityV) * 0.5;
        }

        // Realistic Rim Bounces
        resolveVectorBounce(ball, hoop.x, hoop.y);
        resolveVectorBounce(ball, hoop.x + hoop.width, hoop.y);
    }

    private void resolveVectorBounce(Ball ball, double rx, double ry) {
        double bx = ball.x + ball.radius;
        double by = ball.y + ball.radius;
        double dx = bx - rx;
        double dy = by - ry;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < ball.radius) {
            ball.hitRim = true;
            // Normal vector
            double nx = dx / dist;
            double ny = dy / dist;

            // Dot product of velocity and normal
            double dot = ball.velocityH * nx + ball.velocityV * ny;

            // Reflect velocity: v = v - 2 * (v.n) * n
            ball.velocityH = (ball.velocityH - 2 * dot * nx) * friction;
            ball.velocityV = (ball.velocityV - 2 * dot * ny) * friction;

            // Push ball out of rim to prevent overlap
            ball.x = rx + nx * ball.radius - ball.radius;
            ball.y = ry + ny * ball.radius - ball.radius;
        }
    }

    public boolean checkScore(Ball b, Hoop h) {
        // Strict top-down check: Ball center must be between rim edges 
        // and moving downward while within a small Y-window
        boolean withinX = (b.x + b.radius > h.x) && (b.x + b.radius < h.x + h.width);
        boolean crossingRimY = (b.y + b.radius > h.y - 5) && (b.y + b.radius < h.y + 10);
        return withinX && crossingRimY && b.velocityV > 0;
    }
}
