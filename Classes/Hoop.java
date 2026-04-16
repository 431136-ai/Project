import java.awt.Rectangle;

public class Hoop {
    double x, y;
    int width = 70;
    int height = 10;
    int rimWidth = 8;
    boolean isOnRight = false; // Toggles when teleporting

    public Hoop(int canvasWidth) {
        teleport(canvasWidth);
    }

    public void teleport(int canvasWidth) {
        isOnRight = !isOnRight; // Swap sides
        
        // Randomize Y between 150 and 400
        this.y = 150 + (Math.random() * 250);

        if (isOnRight) {
            // Teleport anywhere on the RIGHT half (padding from edges)
            this.x = (canvasWidth / 2) + 50 + (Math.random() * ((canvasWidth / 2) - 150));
        } else {
            // Teleport anywhere on the LEFT half
            this.x = 50 + (Math.random() * ((canvasWidth / 2) - 150));
        }
    }

    // Physical bounds for bouncing
    public Rectangle getLeftRim() {
        return new Rectangle((int)x, (int)y, rimWidth, height);
    }

    public Rectangle getRightRim() {
        return new Rectangle((int)(x + width - rimWidth), (int)y, rimWidth, height);
    }
}
