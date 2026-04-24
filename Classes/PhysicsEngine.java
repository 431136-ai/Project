public class PhysicsEngine {
    double gravity = 0.45;
    double friction = 0.65; 
    double moveSpeed = 4.5; // Constant forward speed

    public void applyPhysics(Ball ball, int canvasWidth, int canvasHeight, Hoop hoop) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;

        // 1. ALWAYS move towards the hoop! No moving backward.
        if (hoop.isOnRight) {
            ball.velocityH = moveSpeed;
        } else {
            ball.velocityH = -moveSpeed;
        }
        ball.x += ball.velocityH;

        // 2. CEILING (Don't die, just bounce down)
        if (ball.y < 0) {
            ball.y = 0;
            ball.velocityV = Math.abs(ball.velocityV) * 0.3; // Soft bump on the ceiling
        }

        // 3. FLOOR (Don't die, just bounce and slide)
        if (ball.y + ball.radius * 2 > canvasHeight) {
            ball.y = canvasHeight - ball.radius * 2;
            ball.velocityV = -Math.abs(ball.velocityV) * 0.5; // Bounce off the floor
        }

        // 4. Rim collisions (Only bounce vertically so it doesn't push you backward!)
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
            double ny = dy/dist;
            
            // Only affect vertical velocity
            if (ball.velocityV > 0 && ny < 0) {
                ball.velocityV = -ball.velocityV * friction;
            }
            
            // Push the ball out of the rim so it doesn't get stuck
            ball.y = ry + ny * ball.radius - ball.radius;
        }
    }

    public boolean checkScore(Ball b, Hoop h) {
        return b.x + b.radius > h.x && b.x + b.radius < h.x + h.width &&
               b.y + b.radius > h.y && b.y + b.radius < h.y + 15 && b.velocityV > 0;
    }
}
