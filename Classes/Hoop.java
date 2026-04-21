public class Hoop {
    double x, y;
    int width = 85; 
    boolean isOnRight = false; // Alternates properly

    public Hoop(int canvasWidth) {
        teleport(canvasWidth);
    }

    public void teleport(int canvasWidth) {
        isOnRight = !isOnRight; 
        this.y = 180 + (Math.random() * 220); // Keep Y random for vertical challenge
        
        // Strictly left or right side placement
        if (isOnRight) {
            this.x = canvasWidth - width - 60; // Far right
        } else {
            this.x = 60; // Far left
        }
    }
}
