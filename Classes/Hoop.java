public class Hoop {
    double x, y;
    int width = 60; 
    boolean isOnRight = false; 

    public Hoop(int canvasWidth) { 
        reset(canvasWidth); 
    }

    public void reset(int canvasWidth) {
        // Swap sides
        isOnRight = !isOnRight; 
        
        // Random Y axis height (between 150 and 400)
        this.y = 150 + (Math.random() * 250); 
        
        // Position on the left or right side, facing inward
        this.x = isOnRight ? canvasWidth - width - 30 : 30;
    }
}
