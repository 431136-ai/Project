import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TapTapShoot extends JPanel implements ActionListener {
    private Ball ball = new Ball();
    private Hoop hoop = new Hoop(800);
    private PhysicsEngine physics = new PhysicsEngine();
    private int score = 0;
    private int gameState = 0; 
    private Timer timer;

    public TapTapShoot() {
        timer = new Timer(16, this);
        timer.start();
        ball.x = 150; // Fix ball position on the left
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

            // Check if ball passed the gate
            if (physics.checkPassThrough(ball, hoop)) {
                score++;
                hoop.reset(getWidth()); // Spawn next gate
            }

            // Lose: Gate passed you, or you hit the top/bottom
            if (hoop.x < ball.x - 50 || ball.y > getHeight() || ball.y < -20) {
                gameState = 2;
            }
        }
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(15, 18, 28)); 
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == 1) {
            // Draw Vertical Neon Gate
            g2.setColor(new Color(0, 255, 200));
            g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            // Top pillar
            g2.drawLine((int)hoop.x, 0, (int)hoop.x, (int)hoop.y);
            // Bottom pillar
            g2.drawLine((int)hoop.x, (int)hoop.y + hoop.height, (int)hoop.x, getHeight());

            // Draw Ball
            g2.setColor(new Color(240, 110, 40));
            g2.fillOval((int)ball.x - ball.radius, (int)ball.y - ball.radius, ball.radius*2, ball.radius*2);

            // UI
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Verdana", Font.BOLD, 25));
            g2.drawString("Score: " + score, 30, 50);
        } else {
            drawMenu(g2);
        }
    }

    private void drawMenu(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        String text = (gameState == 0) ? "VERTICAL SHOOTER" : "GAME OVER! Score: " + score;
        g2.setFont(new Font("Impact", Font.PLAIN, 50));
        g2.drawString(text, (getWidth() - g2.getFontMetrics().stringWidth(text))/2, 250);
        
        g2.setFont(new Font("Verdana", Font.PLAIN, 18));
        String sub = "Press SPACE to Fly through the gates";
        g2.drawString(sub, (getWidth() - g2.getFontMetrics().stringWidth(sub))/2, 300);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Vertical Tap Tap");
        f.add(new TapTapShoot());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
