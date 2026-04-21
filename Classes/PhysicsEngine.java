public class PhysicsEngine {
    double gravity = 0.48;
    double friction = 0.7; 

    public void applyPhysics(Ball ball, int canvasWidth, Hoop hoop) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;

        // Force direction toward the hoop
        if (hoop.isOnRight && ball.velocityH < 0) ball.velocityH *= -1;
        if (!hoop.isOnRight && ball.velocityH > 0) ball.velocityH *= -1;
        
        // Enforce constant horizontal speed (unless bouncing extremely fast)
        double targetSpeed = 4.0;
        if (Math.abs(ball.velocityH) < targetSpeed) {
            ball.velocityH = (ball.velocityH > 0) ? targetSpeed : -targetSpeed;
        } else if (Math.abs(ball.velocityH) > targetSpeed) {
            ball.velocityH *= 0.95; // Quickly decay back to normal speed if bounced
        }
        
        ball.x += ball.velocityH;
        ball.wrapAround(canvasWidth);

        // Check bounce on Left Rim Tip and Right Rim Tip
        resolveRimBounce(ball, hoop.x, hoop.y);
        resolveRimBounce(ball, hoop.x + hoop.width, hoop.y);
    }

    private void resolveRimBounce(Ball ball, double rimX, double rimY) {
        double ballCenterX = ball.x + ball.radius;
        double ballCenterY = ball.y + ball.radius;
        double dx = ballCenterX - rimX;
        double dy = ballCenterY - rimY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < ball.radius) {
            ball.hitRim = true;
            double nx = dx / distance;
            double ny = dy / distance;
            double dot = ball.velocityH * nx + ball.velocityV * ny;

            ball.velocityH = (ball.velocityH - 2 * dot * nx) * friction;
            ball.velocityV = (ball.velocityV - 2 * dot * ny) * friction;

            ball.x = rimX + nx * ball.radius - ball.radius;
            ball.y = rimY + ny * ball.radius - ball.radius;
        }
    }

    public boolean checkScore(Ball ball, Hoop hoop) {
        return ball.x + ball.radius > hoop.x && 
               ball.x + ball.radius < hoop.x + hoop.width &&
               ball.y + ball.radius > hoop.y && 
               ball.y + ball.radius < hoop.y + 15 &&
               ball.velocityV > 0;
    }
}
