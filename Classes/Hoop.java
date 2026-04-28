public class Hoop {
    double x, y;
    int width = 70; 
    int thickness = 8;
    int netHeight = 60;
    boolean isOnRight = false; 

    public Hoop(int canvasWidth) { reset(canvasWidth); }

    public void reset(int canvasWidth) {
        isOnRight = !isOnRight; 
        this.y = 150 + (Math.random() * 250); 
        updateResize(canvasWidth);
    }

    public void updateResize(int canvasWidth) {
        this.x = isOnRight ? canvasWidth - width - 50 : 50;
    }
}
