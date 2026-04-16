public class PhysicsEngine {
    double gravity = 0.5;

    public void applyPhysics(Ball ball, int canvasWidth, Hoop hoop, Backboard backboard) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;
        ball.x += ball.velocityH;

        ball.wrapAround(canvasWidth);

        // Backboard collision logic
        java.awt.Rectangle boardRect = backboard.getBounds(hoop);
        if (boardRect.intersects(ball.x, ball.y, ball.radius * 2, ball.radius * 2)) {
            ball.velocityH *= -1; // Bounce horizontally
        }
    }

    public boolean checkScore(Ball ball, Hoop hoop) {
        return ball.x + ball.radius > hoop.x && 
               ball.x < hoop.x + hoop.width &&
               ball.y > hoop.y && 
               ball.y < hoop.y + hoop.height &&
               ball.velocityV > 0;
    }
}
