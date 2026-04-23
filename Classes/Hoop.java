public class Hoop {
    double x, y;
    int width = 45; // Pro size
    boolean isOnRight = false; 

    public Hoop(int w) { 
        teleport(w); 
    }

    public void teleport(int w) {
        isOnRight = !isOnRight; // Strict alternation
        // Random height between 180 and 400
        this.y = 180 + (Math.random() * 220); 
        // Position it on the far left or far right
        this.x = isOnRight ? w - width - 80 : 80;
    }
}
