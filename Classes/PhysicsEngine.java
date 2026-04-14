public class PhysicsEngine {
    double gravity = 0.5;

    public void applyPhysics(Ball ball, int canvasWidth) {
        // Vertical
        ball.velocityV += gravity;
        ball.y += ball.velocityV;
        
        // Horizontal
        ball.updateHorizontal(canvasWidth);
    }

    public boolean checkScore(Ball ball, Hoop hoop) {
        return ball.x + ball.radius > hoop.x && 
               ball.x < hoop.x + hoop.width &&
               ball.y > hoop.y && 
               ball.y < hoop.y + hoop.height &&
               ball.velocityV > 0;
    }
}
