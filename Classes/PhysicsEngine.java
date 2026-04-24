public class PhysicsEngine {
    double gravity = 0.45;
    double friction = 0.65; 
    double moveSpeed = 4.5;

    public void applyPhysics(Ball ball, int canvasWidth, int canvasHeight, Hoop hoop) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;

        // Constant horizontal movement based on hoop side
        if (hoop.isOnRight) {
            ball.velocityH = moveSpeed;
        } else {
            ball.velocityH = -moveSpeed;
        }
        ball.x += ball.velocityH;

        // --- SCREEN WRAPPING (Pac-Man Effect) ---
        // Backboard is now purely visual; ball passes through to wrap around
        if (ball.x > canvasWidth) {
            ball.x = -ball.radius * 2;
        } else if (ball.x + ball.radius * 2 < 0) {
            ball.x = canvasWidth;
        }

        // Ceiling Boundary (Bounce down)
        if (ball.y < 0) {
            ball.y = 0;
            ball.velocityV = Math.abs(ball.velocityV) * 0.3;
        }

        // Floor Boundary (Bounce up)
        if (ball.y + ball.radius * 2 > canvasHeight) {
            ball.y = canvasHeight - ball.radius * 2;
            ball.velocityV = -Math.abs(ball.velocityV) * 0.5;
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
            double ny = dy/dist;
            
            // Bounce vertically off the rim
            if (ball.velocityV > 0 && ny < 0) {
                ball.velocityV = -ball.velocityV * friction;
            }
            
            ball.y = ry + ny * ball.radius - ball.radius;
        }
    }

    public boolean checkScore(Ball b, Hoop h) {
        // Scoring condition: passing downward through the hoop's center
        return b.x + b.radius > h.x && b.x + b.radius < h.x + h.width &&
               b.y + b.radius > h.y && b.y + b.radius < h.y + 15 && b.velocityV > 0;
    }
}
