
public class PhysicsEngine {
    double gravity = 0.45;
    double friction = 0.65; 
    double moveSpeed = 4.5; // Constant forward speed

    public void applyPhysics(Ball ball, int canvasWidth, int canvasHeight, Hoop hoop) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;

        // 1. Move towards the hoop
        if (hoop.isOnRight) {
            ball.velocityH = moveSpeed;
        } else {
            ball.velocityH = -moveSpeed;
        }
        ball.x += ball.velocityH;

        // --- 2. SCREEN WRAPPING (The Pac-Man Effect) ---
        // If it goes entirely past the right edge, teleport to the left
        if (ball.x > canvasWidth) {
            ball.x = -ball.radius * 2; 
        } 
        // If it goes entirely past the left edge, teleport to the right
        else if (ball.x + ball.radius * 2 < 0) {
            ball.x = canvasWidth;
        }

        // 3. CEILING (Don't die, just bounce down)
        if (ball.y < 0) {
            ball.y = 0;
            ball.velocityV = Math.abs(ball.velocityV) * 0.3; // Soft bump
        }

        // 4. FLOOR (Don't die, just bounce and slide)
        if (ball.y + ball.radius * 2 > canvasHeight) {
            ball.y = canvasHeight - ball.radius * 2;
            ball.velocityV = -Math.abs(ball.velocityV) * 0.5; // Bounce off the floor
        }

        // 5. Rim collisions
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
            
            // Only affect vertical velocity so it doesn't push you backward
            if (ball.velocityV > 0 && ny < 0) {
                ball.velocityV = -ball.velocityV * friction;
            }
            
            ball.y = ry + ny * ball.radius - ball.radius;
        }
    }

    public boolean checkScore(Ball b, Hoop h) {
        return b.x + b.radius > h.x && b.x + b.radius < h.x + h.width &&
               b.y + b.radius > h.y && b.y + b.radius < h.y + 15 && b.velocityV > 0;
    }
}
