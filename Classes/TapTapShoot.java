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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState == 1) {
            hoop.updateResize(getWidth()); 
            physics.applyPhysics(ball, getWidth(), getHeight(), hoop);
            timeManager.update();

            if (physics.checkScore(ball, hoop)) {
                scoreManager.scoreBasket(!ball.hitRim);
                hoop.reset(getWidth()); 
                timeManager.reset(); 
                ball.hitRim = false; 
            }

            if (timeManager.isTimeUp()) gameState = 2;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(new Color(15, 18, 28)); 
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == 0) {
            drawUI(g2, "TAP TAP SHOOT", "PRESS SPACE TO START");
        } else if (gameState == 2) {
            drawUI(g2, "GAME OVER", "SCORE: " + scoreManager.getScore() + " - SPACE TO RETRY");
        } else {
            drawGameObjects(g2);
            drawHUD(g2);
        }
    }

    private void drawGameObjects(Graphics2D g2) {
        // 1. THE NET (Diamond Mesh)
        g2.setColor(new Color(255, 255, 255, 130));
        g2.setStroke(new BasicStroke(1.2f));
        int segments = 8;
        for (int i = 0; i <= segments; i++) {
            int topX = (int)hoop.x + (i * hoop.width / segments);
            int botX = (int)hoop.x + (hoop.width/4) + (i * (hoop.width/2) / segments);
            g2.drawLine(topX, (int)hoop.y, botX, (int)hoop.y + hoop.netHeight);
            if (i < segments) {
                int nextBotX = (int)hoop.x + (hoop.width/4) + ((i+1) * (hoop.width/2) / segments);
                g2.drawLine(topX, (int)hoop.y, nextBotX, (int)hoop.y + (hoop.netHeight / 2));
            }
        }

        // 2. THE BALL (With 3D ribs)
        g2.setColor(new Color(230, 90, 30));
        g2.fillOval((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);
        g2.setColor(new Color(0, 0, 0, 80));
        g2.setStroke(new BasicStroke(2f));
        g2.drawArc((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2, 45, 180);
        g2.drawArc((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2, 45, -180);

        // 3. THE RIM (Thinner & Realistic)
        g2.setColor(new Color(150, 20, 0)); 
        g2.setStroke(new BasicStroke(hoop.thickness + 2));
        g2.drawOval((int)hoop.x, (int)hoop.y - 4, hoop.width, 12); // Shadow layer
        g2.setColor(new Color(255, 60, 0));
        g2.setStroke(new BasicStroke(hoop.thickness));
        g2.drawOval((int)hoop.x, (int)hoop.y - 5, hoop.width, 10); // Top layer
        
        // Wall Attachment
        g2.setColor(Color.GRAY);
        if (hoop.isOnRight) g2.fillRect((int)hoop.x + hoop.width, (int)hoop.y - 2, 60, 4);
        else g2.fillRect(0, (int)hoop.y - 2, (int)hoop.x, 4);
    }

    private void drawHUD(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        g2.drawString("SCORE: " + scoreManager.getScore(), 20, 40);

        // Timer Bar
        int barW = 200;
        g2.setColor(Color.DARK_GRAY);
        g2.fillRoundRect(getWidth()/2 - barW/2, 20, barW, 10, 5, 5);
        g2.setColor(timeManager.timeLeft < 30 ? Color.RED : Color.GREEN);
        g2.fillRoundRect(getWidth()/2 - barW/2, 20, (int)(timeManager.timeLeft * (barW/100.0)), 10, 5, 5);
    }

    private void drawUI(Graphics2D g2, String main, String sub) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Impact", Font.PLAIN, 50));
        g2.drawString(main, (getWidth() - g2.getFontMetrics().stringWidth(main))/2, getHeight()/2);
        g2.setFont(new Font("Verdana", Font.PLAIN, 18));
        g2.drawString(sub, (getWidth() - g2.getFontMetrics().stringWidth(sub))/2, getHeight()/2 + 50);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Tap Tap Shootout");
        f.add(new TapTapShoot());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
