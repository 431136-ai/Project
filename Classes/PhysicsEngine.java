public class PhysicsEngine {
    double gravity = 0.45;
    double friction = 0.65; 
    double targetX = 250; // The resting X position of the ball

    public void applyPhysics(Ball ball, Hoop hoop) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;

        // Soft spring: gently pulls the ball back to the left side of the screen
        ball.velocityH += (targetX - ball.x) * 0.05;
        ball.velocityH *= 0.9; // Dampens the bouncing so it doesn't swing wildly
        ball.x += ball.velocityH;

        resolveRimBounce(ball, hoop.x, hoop.y, hoop.speed);
        resolveRimBounce(ball, hoop.x + hoop.width, hoop.y, hoop.speed);
    }

    private void resolveRimBounce(Ball ball, double rx, double ry, double hoopSpeed) {
        double bx = ball.x + ball.radius;
        double by = ball.y + ball.radius;
        double dx = bx - rx, dy = by - ry;
        double dist = Math.sqrt(dx*dx + dy*dy);

        if (dist < ball.radius) {
            ball.hitRim = true;
            double nx = dx/dist, ny = dy/dist;
            
            // Factor in the hoop's movement for realistic bouncing
            double relVelH = ball.velocityH - (-hoopSpeed); 
            double dot = relVelH * nx + ball.velocityV * ny;
            
            ball.velocityH = (ball.velocityH - 2 * dot * nx) * friction;
            ball.velocityV = (ball.velocityV - 2 * dot * ny) * friction;
            
            ball.x = rx + nx * ball.radius - ball.radius;
            ball.y = ry + ny * ball.radius - ball.radius;
        }
    }

    public boolean checkScore(Ball b, Hoop h) {
        // Must pass through the TOP of the hoop
        return b.x + b.radius > h.x && b.x + b.radius < h.x + h.width &&
               b.y + b.radius > h.y && b.y + b.radius < h.y + 15 && b.velocityV > 0;
    }
}
