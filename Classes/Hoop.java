public class Hoop {
    double x, y;
    int width = 75; 
    int thickness = 5; // Reduced from 8 for a sleeker look
    int netHeight = 70;
    boolean isOnRight = false; 

    public Hoop(int canvasWidth) { reset(canvasWidth); }

    public void reset(int canvasWidth) {
        isOnRight = !isOnRight; 
        this.y = 100 + (Math.random() * 300); 
        updateResize(canvasWidth);
    }

    public void updateResize(int canvasWidth) {
        // Added a bit more padding from the wall for realism
        this.x = isOnRight ? canvasWidth - width - 60 : 60;
    }
}
