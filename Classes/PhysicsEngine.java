public class PhysicsEngine {
    double gravity = 0.48;
    double friction = 0.7; // Energy kept after a bounce

    public void applyPhysics(Ball ball, int canvasWidth, Hoop hoop) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;

        // Force ball to face the hoop
        if (hoop.isOnRight && ball.velocityH < 0) ball.velocityH *= -1;
        if (!hoop.isOnRight && ball.velocityH > 0) ball.velocityH *= -1;
        
        ball.x += ball.velocityH;
        ball.wrapAround(canvasWidth);

        // Check bounce on Left Rim Tip and Right Rim Tip
        resolveRimBounce(ball, hoop.x, hoop.y);
        resolveRimBounce(ball, hoop.x + hoop.width, hoop.y);
    }

    private void resolveRimBounce(Ball ball, double rimX, double rimY) {
        double ballCenterX = ball.x + ball.radius;
        double ballCenterY = ball.y + ball.radius;

        // Pythagorean theorem to find distance to the rim tip
        double dx = ballCenterX - rimX;
        double dy = ballCenterY - rimY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < ball.radius) {
            ball.hitRim = true;

            // Calculate Normal vector (the direction from rim to ball)
            double nx = dx / distance;
            double ny = dy / distance;

            // Dot product for reflection
            double dot = ball.velocityH * nx + ball.velocityV * ny;

            // Reflect velocity: v = v - 2(v.n)n
            ball.velocityH = (ball.velocityH - 2 * dot * nx) * friction;
            ball.velocityV = (ball.velocityV - 2 * dot * ny) * friction;

            // Push ball out of the rim to prevent sticking
            ball.x = rimX + nx * ball.radius - ball.radius;
            ball.y = rimY + ny * ball.radius - ball.radius;
        }
    }

    public boolean checkScore(Ball ball, Hoop hoop) {
        // Scored if center passes through the hoop line while falling
        return ball.x + ball.radius > hoop.x && 
               ball.x + ball.radius < hoop.x + hoop.width &&
               ball.y + ball.radius > hoop.y && 
               ball.y + ball.radius < hoop.y + 15 &&
               ball.velocityV > 0;
    }
}
