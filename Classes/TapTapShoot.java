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
        hoop = new Hoop(800); 
        ball.velocityH = hoop.isOnRight ? 4.0 : -4.0;
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
                ball.y = -30; // Reset Y to top
                ball.velocityH = hoop.isOnRight ? 4.0 : -4.0;
                ball.velocityV = 2; 
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

        g2.setColor(new Color(15, 18, 28)); // Background
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == 0) {
            drawHomeScreen(g2);
        } else {
            // Draw Hoop
            g2.setColor(new Color(255, 60, 0));
            g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine((int)hoop.x, (int)hoop.y, (int)(hoop.x + hoop.width), (int)hoop.y);
            
            // Draw Ball
            g2.setColor(new Color(240, 110, 40));
            g2.fillOval((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);

            // Score & Streak UI
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Verdana", Font.BOLD, 22));
            g2.drawString("Score: " + scoreManager.getScore(), 30, 45);
            if (scoreManager.getStreak() > 0) {
                g2.setColor(new Color(255, 200, 0));
                g2.drawString("STREAK x" + scoreManager.getStreak() + " 🔥", 30, 80);
            }

            // Time Bar
            g2.setColor(new Color(40, 40, 50));
            g2.fillRoundRect(getWidth()/2 - 150, 25, 300, 12, 10, 10);
            g2.setColor(timeManager.timeLeft < 30 ? Color.RED : Color.GREEN);
            g2.fillRoundRect(getWidth()/2 - 150, 25, (int)(3 * timeManager.timeLeft), 12, 10, 10);

            if (gameState == 2) drawGameOverScreen(g2);
        }
    }

    private void drawHomeScreen(Graphics2D g) {
        g.setFont(new Font("Impact", Font.ITALIC, 70));
        g.setColor(new Color(255, 60, 0, 100));
        String title = "TAP TAP SHOOT";
        int tx = (getWidth() - g.getFontMetrics().stringWidth(title)) / 2;
        g.drawString(title, tx + 4, 204); // Shadow
        g.setColor(Color.WHITE);
        g.drawString(title, tx, 200);

        g.setFont(new Font("Verdana", Font.PLAIN, 18));
        g.setColor(new Color(200, 200, 200));
        String[] lines = {"Tap SPACE to fly", "Score clean swishes for streaks", "Don't let the timer hit zero!", "", "PRESS [SPACE] TO START"};
        int startY = 280;
        for (String line : lines) {
            g.drawString(line, (getWidth() - g.getFontMetrics().stringWidth(line)) / 2, startY);
            startY += 30;
        }
    }

    private void drawGameOverScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        g.setFont(new Font("Impact", Font.PLAIN, 60));
        String msg = "GAME OVER";
        g.drawString(msg, (getWidth() - g.getFontMetrics().stringWidth(msg)) / 2, 250);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Verdana", Font.BOLD, 24));
        String score = "Score: " + scoreManager.getScore();
        g.drawString(score, (getWidth() - g.getFontMetrics().stringWidth(score)) / 2, 310);
        g.setFont(new Font("Verdana", Font.PLAIN, 16));
        g.drawString("Press [SPACE] to Restart", (getWidth() - g.getFontMetrics().stringWidth("Press [SPACE] to Restart")) / 2, 360);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Tap Tap Shoot Pro");
        f.add(new TapTapShoot());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
