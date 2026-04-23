import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TapTapShoot extends JPanel implements ActionListener {
    private Ball ball = new Ball();
    private Hoop hoop = new Hoop(800);
    private PhysicsEngine physics = new PhysicsEngine();
    private int score = 0;
    private int gameState = 0; // 0: Menu, 1: Play, 2: Dead
    private Timer timer;

    public TapTapShoot() {
        timer = new Timer(16, this);
        timer.start();
        ball.x = 150; // Ball stays on the left side
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gameState == 0) gameState = 1;
                    else if (gameState == 1) ball.jump();
                    else restart();
                }
            }
        });
    }

    private void restart() {
        ball = new Ball();
        ball.x = 150;
        hoop.reset(800);
        score = 0;
        gameState = 1;
    }

    public void actionPerformed(ActionEvent e) {
        if (gameState == 1) {
            physics.applyPhysics(ball);
            hoop.move();

            // Check Score
            if (physics.checkPassThrough(ball, hoop)) {
                score++;
                hoop.reset(800); // Send next hoop
            }

            // LOSE CONDITION: Hoop missed or Ball hits floor/ceiling
            if (hoop.x < -20 || ball.y > 600 || ball.y < -50) {
                gameState = 2;
            }
        }
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(new Color(10, 15, 25));
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == 1) {
            // Draw Vertical Hoop (The Gate)
            g2.setColor(Color.CYAN);
            g2.setStroke(new BasicStroke(8));
            // Draw top and bottom "posts" of the hoop
            g2.drawLine((int)hoop.x, 0, (int)hoop.x, (int)hoop.y);
            g2.drawLine((int)hoop.x, (int)hoop.y + hoop.height, (int)hoop.x, getHeight());

            // Draw Ball
            g2.setColor(Color.ORANGE);
            g2.fillOval((int)ball.x - ball.radius, (int)ball.y - ball.radius, ball.radius*2, ball.radius*2);

            // Score
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            g2.drawString("Score: " + score, 30, 50);
        } else if (gameState == 2) {
            g2.setColor(Color.WHITE);
            g2.drawString("MISSED THE HOOP! Final Score: " + score, 200, 300);
            g2.drawString("Press Space to Restart", 280, 350);
        } else {
            g2.setColor(Color.WHITE);
            g2.drawString("FLAPPY HOOPS: VERTICAL EDITION", 200, 250);
            g2.drawString("Pass through the gates. Don't let them pass you!", 180, 300);
            g2.drawString("Press Space to Start", 300, 350);
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.add(new TapTapShoot());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
