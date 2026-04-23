public class Hoop {
    double x, y;
    int width = 60; // Horizontal basket
    double speed = 4.0; // The scrolling speed
    boolean isScored = false; 

    public Hoop(int canvasWidth) { 
        reset(canvasWidth); 
    }

    public void reset(int canvasWidth) {
        this.x = canvasWidth + 50; // Spawn off-screen to the right
        this.y = 150 + (Math.random() * 250); // Random height
        this.isScored = false;
    }

    public void move() {
        this.x -= speed; // Move towards the left
    }
}
