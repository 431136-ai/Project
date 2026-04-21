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
    private int gameState = 0; 

    public TapTapShoot() {
        timer = new Timer(16, this);
        timer.start();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gameState == 0) gameState = 1;
                    else if (gameState == 1) ball.jump();
                    else resetGame();
                }
            }
        });
    }

    private void resetGame() {
        ball = new Ball();
        hoop.teleport(800);
        scoreManager.reset();
        timeManager.reset();
        gameState = 1;
    }

    public void actionPerformed(ActionEvent e) {
        if (gameState == 1) {
            physics.applyPhysics(ball, getWidth(), hoop);
            timeManager.update();

            if (physics.checkScore(ball, hoop)) {
                scoreManager.scoreBasket(!ball.hitRim);
                timeManager.reset();
                hoop.teleport(getWidth());
                ball.y = -50; 
                ball.velocityV = 2; // Slight downward start
                ball.hitRim = false; 
            }

            if (ball.y > getHeight() || timeManager.isTimeUp()) gameState = 2;
        }
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(20, 24, 35)); // Sleek dark blue/gray
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == 0) {
            drawScreen(g2, "TAP TAP SHOOT", "Press SPACE to Play");
        } else {
            // Draw Hoop (A clean orange line with a net)
            g2.setColor(new Color(255, 80, 0));
            g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine((int)hoop.x, (int)hoop.y, (int)(hoop.x + hoop.width), (int)hoop.y);
            
            // Draw Net
            g2.setColor(new Color(255, 255, 255, 80));
            g2.setStroke(new BasicStroke(1));
            g2.drawLine((int)hoop.x, (int)hoop.y, (int)hoop.x + 8, (int)hoop.y + 35);
            g2.drawLine((int)hoop.x + hoop.width, (int)hoop.y, (int)hoop.x + hoop.width - 8, (int)hoop.y + 35);

            // Draw Ball
            g2.setColor(new Color(240, 100, 30));
            g2.fillOval((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);

            // UI
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            g2.drawString("Score: " + scoreManager.getScore(), 30, 45);
            if (scoreManager.getStreak() > 0) {
                g2.setColor(Color.ORANGE);
                g2.drawString("STREAK x" + scoreManager.getStreak() + " 🔥", 30, 75);
            }

            // Time Bar
            g2.setColor(Color.DARK_GRAY);
            g2.fillRoundRect(getWidth()/2 - 150, 25, 300, 12, 10, 10);
            g2.setColor(timeManager.timeLeft < 30 ? Color.RED : Color.GREEN);
            g2.fillRoundRect(getWidth()/2 - 150, 25, (int)(3 * timeManager.timeLeft), 12, 10, 10);

            if (gameState == 2) drawScreen(g2, "GAME OVER", "Final Score: " + scoreManager.getScore());
        }
    }

    private void drawScreen(Graphics2D g, String main, String sub) {
        g.setColor(new Color(0,0,0,180));
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 45));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(main, (getWidth() - fm.stringWidth(main)) / 2, 260);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        fm = g.getFontMetrics();
        g.drawString(sub, (getWidth() - fm.stringWidth(sub)) / 2, 310);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Tap Tap Shoot Classic");
        f.add(new TapTapShoot());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
    }
}
