import java.awt.Rectangle;

public class PhysicsEngine {
    double gravity = 0.5;

    public void applyPhysics(Ball ball, int canvasWidth, Hoop hoop, Backboard backboard) {
        // 1. Apply Gravity
        ball.velocityV += gravity;
        ball.y += ball.velocityV;

        // 2. Force ball to always move towards the hoop
        if (hoop.isOnRight && ball.velocityH < 0) ball.velocityH *= -1;
        if (!hoop.isOnRight && ball.velocityH > 0) ball.velocityH *= -1;
        
        ball.x += ball.velocityH;
        ball.wrapAround(canvasWidth);

        Rectangle ballRect = new Rectangle((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);

        // 3. Backboard Collision (Bounce Away)
        Rectangle boardRect = backboard.getBounds(hoop);
        if (ballRect.intersects(boardRect)) {
            ball.hitRimOrBoard = true;
            if (hoop.isOnRight) {
                ball.velocityH = -Math.abs(ball.velocityH); // Force bounce left
                ball.x = boardRect.x - ball.radius*2 - 1;   // Push out of backboard
            } else {
                ball.velocityH = Math.abs(ball.velocityH);  // Force bounce right
                ball.x = boardRect.x + boardRect.width + 1; // Push out
            }
        }

        // 4. Rim Collision (Pop up)
        if (ballRect.intersects(hoop.getLeftRim()) || ballRect.intersects(hoop.getRightRim())) {
            ball.hitRimOrBoard = true;
            // Pop the ball up into the air slightly when it hits a rim
            if (ball.velocityV > 0) {
                ball.velocityV = -Math.abs(ball.velocityV) * 0.6; 
            }
        }
    }

    public boolean checkScore(Ball ball, Hoop hoop) {
        // Must pass through the gap between the two rims while falling
        return ball.x + ball.radius > hoop.x + hoop.rimWidth && 
               ball.x + ball.radius < hoop.x + hoop.width - hoop.rimWidth &&
               ball.y + (ball.radius*2) > hoop.y && 
               ball.y < hoop.y + hoop.height &&
               ball.velocityV > 0;
    }
}
