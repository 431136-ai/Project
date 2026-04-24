import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TapTapShoot extends JPanel implements ActionListener {
    private Ball ball = new Ball();
    private Hoop hoop = new Hoop(800); 
    private PhysicsEngine physics = new PhysicsEngine();
    private ScoreManager scoreManager = new ScoreManager();
    private TimeManager timeManager = new TimeManager();
    private Timer timer;
    private int gameState = 0; // 0: Menu, 1: Play, 2: Game Over

    public TapTapShoot() {
        timer = new Timer(16, this);
        timer.start();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gameState == 0 || gameState == 2) resetGame();
                    else ball.jump();
                }
            }
        });
    }

    private void resetGame() {
        ball = new Ball();
        hoop.reset(getWidth());
        scoreManager.reset();
        timeManager.reset();
        gameState = 1;
    }

    public void actionPerformed(ActionEvent e) {
        if (gameState == 1) {
            hoop.updateResize(getWidth()); 
            physics.applyPhysics(ball, getWidth(), getHeight(), hoop);
            timeManager.update();

            if (physics.checkScore(ball, hoop)) {
                scoreManager.scoreBasket(!ball.hitRim);
                hoop.reset(getWidth()); 
                timeManager.reset(); // Refill time on score
                ball.hitRim = false; 
            }

            if (timeManager.isTimeUp()) gameState = 2;
        }
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(15, 18, 28)); 
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == 0) {
            drawUI(g2, "TAP TAP SHOOT", "Press SPACE to Start");
        } else if (gameState == 2) {
            drawUI(g2, "GAME OVER", "Final Score: " + scoreManager.getScore() + " - Space to Retry");
        } else {
            drawGameObjects(g2);
        }
    }

    private void drawGameObjects(Graphics2D g2) {
        // Backboard
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4));
        int bbX = hoop.isOnRight ? (int)(hoop.x + hoop.width + 5) : (int)(hoop.x - 5);
        g2.drawLine(bbX, (int)hoop.y - 40, bbX, (int)hoop.y + 20);

        // Net & Rim (omitted for brevity, keep your existing drawing code here)
        g2.setColor(new Color(255, 60, 0));
        g2.drawLine((int)hoop.x, (int)hoop.y, (int)(hoop.x + hoop.width), (int)hoop.y);

        // Ball
        g2.setColor(new Color(240, 110, 40));
        g2.fillOval((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);

        // Timer Bar
        g2.setColor(new Color(40, 45, 60));
        g2.fillRoundRect(getWidth()/2 - 100, 20, 200, 10, 5, 5);
        g2.setColor(timeManager.timeLeft < 30 ? Color.RED : Color.GREEN);
        g2.fillRoundRect(getWidth()/2 - 100, 20, (int)(timeManager.timeLeft * 2), 10, 5, 5);

        // Score
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Verdana", Font.BOLD, 20));
        g2.drawString("Score: " + scoreManager.getScore(), 20, 40);
    }

    private void drawUI(Graphics2D g2, String main, String sub) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Impact", Font.PLAIN, 50));
        g2.drawString(main, (getWidth() - g2.getFontMetrics().stringWidth(main))/2, getHeight()/2);
        g2.setFont(new Font("Verdana", Font.PLAIN, 18));
        g2.drawString(sub, (getWidth() - g2.getFontMetrics().stringWidth(sub))/2, getHeight()/2 + 50);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Tap Tap Pro");
        f.add(new TapTapShoot());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
