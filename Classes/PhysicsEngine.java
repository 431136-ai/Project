public class PhysicsEngine {
    double gravity = 0.5;

    public void applyGravity(Ball ball) {
        ball.velocityV += gravity;
        ball.y += ball.velocityV;
    }

    public boolean checkScore(Ball ball, Hoop hoop) {
        // Checks if ball is within horizontal bounds of hoop 
        // and passing through vertically while falling
        return ball.x > hoop.x && 
               ball.x < hoop.x + hoop.width &&
               ball.y > hoop.y && 
               ball.y < hoop.y + hoop.height &&
               ball.velocityV > 0;
    }
}
