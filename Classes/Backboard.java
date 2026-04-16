import java.awt.Rectangle;

public class Backboard {
    int width = 10;
    int height = 80;
    
    public Rectangle getBounds(Hoop hoop) {
        // If hoop is on the right, backboard is to its right. 
        // If hoop is on the left, backboard is to its left.
        int xPos = hoop.isOnRight ? (int)hoop.x - 10 : (int)hoop.x + hoop.width;
        return new Rectangle(xPos, (int)hoop.y - 40, width, height);
    }
}
