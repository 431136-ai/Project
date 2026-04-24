public class Hoop {
    double x, y;
    int width = 60; 
    boolean isOnRight = false; 

    public Hoop(int canvasWidth) { 
        reset(canvasWidth); 
    }

    public void reset(int canvasWidth) {
        isOnRight = !isOnRight; 
        this.y = 150 + (Math.random() * 250); 
        updateResize(canvasWidth);
    }

    // Keeps the hoop on the correct wall if the window goes fullscreen
    public void updateResize(int canvasWidth) {
        this.x = isOnRight ? canvasWidth - width - 30 : 30;
    }
}
