public class Hoop {
    double x;
    double y = 200;
    int width = 80;
    int height = 15;
    double speed = 3;
    int direction = 1;

    public Hoop(int canvasWidth) {
        this.x = canvasWidth - 150;
    }

    public void update(int canvasHeight) {
        y += speed * direction;
        // Bounce off top and bottom boundaries
        if (y > canvasHeight - 150 || y < 50) {
            direction *= -1;
        }
    }
}

