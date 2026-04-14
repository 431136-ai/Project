import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TapTapShoot extends JPanel implements ActionListener {
    private Ball ball;
    private Hoop hoop;
    private PhysicsEngine physics;
    private ScoreManager scoreManager;
    private Timer timer;
    private boolean gameActive = true;

    public TapTapShoot() {
        ball = new Ball();
        hoop = new Hoop(800);
        physics = new PhysicsEngine();
        scoreManager = new ScoreManager();
        
        timer = new Timer(16, this); // roughly 60 FPS
        timer.start();

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gameActive) {
                        ball.jump();
                    } else {
                        resetGame();
                    }
                }
            }
        });
    }

    private void resetGame() {
        ball = new Ball();
        scoreManager.reset();
        gameActive = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameActive) {
            physics.applyGravity(ball);
            hoop.update(getHeight());

            if (physics.checkScore(ball, hoop)) {
                scoreManager.increment();
                // Reset ball position for the next shot
                ball.y = 50; 
                ball.velocityV = 0;
            }

            // Lose condition: fall out of bounds
            if (ball.y > getHeight()) {
                gameActive = false;
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Background
        g.setColor(new Color(135, 206, 235)); // Sky Blue
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw Ball
        g.setColor(Color.ORANGE);
        g.fillOval((int)ball.x, (int)ball.y, ball.radius * 2, ball.radius * 2);

        // Draw Hoop
        g.setColor(Color.RED);
        g.fillRect((int)hoop.x, (int)hoop.y, hoop.width, hoop.height);

        // Draw UI
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + scoreManager.getScore(), 20, 30);

        if (!gameActive) {
            g.drawString("GAME OVER - Press Space to Restart", 200, 300);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tap Tap Shoot");
        TapTapShoot game = new TapTapShoot();
        frame.add(game);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
