public class PhysicsEngine {
    double gravity = 0.45;
    double friction = 0.65; 
    double minH = 4.0; // The fixed minimum speed

    public void applyPhysics(Ball ball, int w, Hoop hoop) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;

        // Force direction AND minimum speed
        double direction = hoop.isOnRight ? 1 : -1;
        
        // This line ensures we never drop below 4.0 horizontal speed
        if (Math.abs(ball.velocityH) < minH || (ball.velocityH * direction < 0)) {
            ball.velocityH = direction * minH;
        }
        
        ball.x += ball.velocityH;
        ball.wrapAround(w);

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
