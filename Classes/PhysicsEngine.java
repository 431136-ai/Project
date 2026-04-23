public class PhysicsEngine {
    double gravity = 0.45;
    double friction = 0.65; 
    double minH = 3.5; // Minimum horizontal speed

    public void applyPhysics(Ball ball, int canvasWidth, Hoop hoop) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;

        // Enforce minimum horizontal speed
        if (Math.abs(ball.velocityH) < minH) {
            ball.velocityH = (ball.velocityH < 0) ? -minH : minH;
        }
        
        ball.x += ball.velocityH;

        // Wall Bouncing (Left and Right edges)
        if (ball.x - ball.radius < 0) {
            ball.x = ball.radius; // Prevent sticking
            ball.velocityH = Math.abs(ball.velocityH); // Force right
        } else if (ball.x + ball.radius > canvasWidth) {
            ball.x = canvasWidth - ball.radius; // Prevent sticking
            ball.velocityH = -Math.abs(ball.velocityH); // Force left
        }

        // Rim collisions
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
            
            ball.x = rx + nx * ball.radius - ball.radius;
            ball.y = ry + ny * ball.radius - ball.radius;
        }
    }

    public boolean checkScore(Ball b, Hoop h) {
        return b.x + b.radius > h.x && b.x + b.radius < h.x + h.width &&
               b.y + b.radius > h.y && b.y + b.radius < h.y + 15 && b.velocityV > 0;
    }
}
