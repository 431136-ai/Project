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
        timer = new Timer(16, this); // ~60 FPS
        timer.start();
        setFocusable(true);
        
        // Control Handling
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gameState == 0 || gameState == 2) {
                        resetGame();
                    } else {
                        ball.jump();
                    }
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

            // Check for Score
            if (physics.checkScore(ball, hoop)) {
                scoreManager.scoreBasket(!ball.hitRim);
                hoop.reset(getWidth()); 
                timeManager.reset(); // Reset shot clock on score
                ball.hitRim = false; 
            }

            // Check for Time Out
            if (timeManager.isTimeUp()) {
                gameState = 2;
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Smoothing for circles and lines
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(new Color(10, 12, 20)); 
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == 0) {
            drawUI(g2, "TAP TAP SHOOT", "PRESS SPACE TO START");
        } else if (gameState == 2) {
            drawUI(g2, "TIME'S UP!", "FINAL SCORE: " + scoreManager.getScore() + " | SPACE TO RETRY");
        } else {
            renderGame(g2);
        }
    }

    private void renderGame(Graphics2D g2) {
        // --- 1. DRAW NET (Behind the ball) ---
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(1.5f));
        int segments = 8;
        for (int i = 0; i <= segments; i++) {
            int startX = (int)hoop.x + (i * hoop.width / segments);
            int endX = (int)hoop.x + (hoop.width/4) + (i * (hoop.width/2) / segments);
            g2.drawLine(startX, (int)hoop.y, endX, (int)hoop.y + hoop.netHeight);
        }

        // --- 2. DRAW BALL ---
        g2.setColor(new Color(255, 100, 0)); // Bright basketball orange
        g2.fillOval((int)ball.x, (int)ball.y, ball.radius * 2, ball.radius * 2);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        g2.drawOval((int)ball.x, (int)ball.y, ball.radius * 2, ball.radius * 2);
        // Ball details (lines)
        g2.drawArc((int)ball.x, (int)ball.y - 5, ball.radius * 2, ball.radius * 2, 0, -180);

        // --- 3. DRAW RIM (In front of ball) ---
        g2.setColor(new Color(255, 50, 0));
        g2.setStroke(new BasicStroke(hoop.thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // Oval gives the "3D" top-down look
        g2.drawOval((int)hoop.x, (int)hoop.y - 5, hoop.width, 10);

        // --- 4. DRAW FLOOR ---
        g2.setColor(new Color(30, 35, 50));
        g2.fillRect(0, getHeight() - 5, getWidth(), 5);

        // --- 5. DRAW UI (Score & Timer) ---
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.BOLD, 25));
        g2.drawString("SCORE: " + scoreManager.getScore(), 30, 50);

        // Timer Bar at top center
        int barWidth = 200;
        int currentBar = (int)(timeManager.timeLeft * (barWidth / timeManager.maxTime));
        g2.setColor(Color.DARK_GRAY);
        g2.fillRoundRect(getWidth()/2 - barWidth/2, 30, barWidth, 12, 10, 10);
        g2.setColor(timeManager.timeLeft < 30 ? Color.RED : new Color(0, 255, 150));
        g2.fillRoundRect(getWidth()/2 - barWidth/2, 30, currentBar, 12, 10, 10);
    }

    private void drawUI(Graphics2D g2, String title, String sub) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Impact", Font.PLAIN, 60));
        int titleWidth = g2.getFontMetrics().stringWidth(title);
        g2.drawString(title, (getWidth() - titleWidth) / 2, getHeight() / 2 - 20);

        g2.setFont(new Font("Verdana", Font.BOLD, 16));
        int subWidth = g2.getFontMetrics().stringWidth(sub);
        g2.drawString(sub, (getWidth() - subWidth) / 2, getHeight() / 2 + 30);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tap Tap Shoot: Vector Edition");
        TapTapShoot game = new TapTapShoot();
        frame.add(game);
        frame.setSize(850, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true); // Supports fullscreen/resizing
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
