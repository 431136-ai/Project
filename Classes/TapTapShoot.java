import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TapTapShoot extends JPanel implements ActionListener {
    private Ball ball;
    private Hoop hoop;
    private PhysicsEngine physics;
    private ScoreManager scoreManager;
    private TimeManager timeManager;
    private Timer timer;
    
    // States: 0 = Home Page, 1 = Playing, 2 = Game Over
    private int gameState = 0; 

    public TapTapShoot() {
        ball = new Ball();
        hoop = new Hoop(800);
        physics = new PhysicsEngine();
        scoreManager = new ScoreManager();
        timeManager = new TimeManager();
        
        timer = new Timer(16, this);
        timer.start();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gameState == 0) gameState = 1; // Start game
                    else if (gameState == 1) ball.jump();
                    else if (gameState == 2) resetGame();
                }
            }
        });
    }

    private void resetGame() {
        ball = new Ball();
        scoreManager.reset();
        timeManager.reset();
        gameState = 1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameState == 1) {
            physics.applyPhysics(ball, getWidth());
            hoop.update(getHeight());
            timeManager.update();

            if (physics.checkScore(ball, hoop)) {
                scoreManager.increment();
                timeManager.addBonus(20); // Add time on score
                ball.y = 50; 
                ball.velocityV = 0;
            }

            if (ball.y > getHeight() || timeManager.isTimeUp()) {
                gameState = 2;
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Background
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == 0) {
            drawHomePage(g2);
        } else {
            drawGame(g2);
        }
    }

    private void drawHomePage(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("TAP TAP SHOOT", 200, 250);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press SPACE to Start", 300, 320);
    }

    private void drawGame(Graphics2D g) {
        // Draw Ball
        g.setColor(Color.ORANGE);
        g.fillOval((int)ball.x, (int)ball.y, ball.radius * 2, ball.radius * 2);

        // Draw Hoop
        g.setColor(Color.RED);
        g.fillRect((int)hoop.x, (int)hoop.y, hoop.width, hoop.height);

        // Draw Time Bar
        g.setColor(Color.GRAY);
        g.fillRect(200, 20, 400, 20);
        g.setColor(timeManager.timeLeft < 30 ? Color.RED : Color.GREEN);
        g.fillRect(200, 20, (int)(4 * timeManager.timeLeft), 20);

        // Draw Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + scoreManager.getScore(), 20, 30);

        if (gameState == 2) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,800,600);
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", 350, 280);
            g.drawString("Press SPACE to Restart", 300, 320);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tap Tap Shoot");
        frame.add(new TapTapShoot());
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
