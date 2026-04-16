import java.awt.Rectangle;

public class Backboard {
    int width = 10;
    int height = 90;
    
    public Rectangle getBounds(Hoop hoop) {
        // Position backboard relative to the hoop's side
        int xPos = hoop.isOnRight ? (int)hoop.x + hoop.width : (int)hoop.x - width;
        return new Rectangle(xPos, (int)hoop.y - 60, width, height);
    }
}
