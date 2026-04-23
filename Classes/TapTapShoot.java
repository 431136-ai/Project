import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TapTapShoot extends JPanel implements ActionListener {
    private Ball ball = new Ball();
    private Hoop hoop = new Hoop(800);
    private PhysicsEngine physics = new PhysicsEngine();
    private ScoreManager scoreManager = new ScoreManager();
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
        scoreManager.reset();
        gameState = 1;
    }

    public void actionPerformed(ActionEvent e) {
        if (gameState == 1) {
            physics.applyPhysics(ball, getWidth(), hoop);

            // Check if scored
            if (physics.checkScore(ball, hoop)) {
                scoreManager.scoreBasket(!ball.hitRim);
                hoop.reset(getWidth()); // Spawn on opposite side
                ball.hitRim = false; 
            }

            // Lose if ball falls off the bottom or hits the top
            if (ball.y > getHeight() || ball.y < -50) gameState = 2;
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
            drawHomeScreen(g2);
        } else {
            // --- DRAW BACKBOARD ---
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(4));
            if (hoop.isOnRight) {
                g2.drawLine((int)(hoop.x + hoop.width + 5), (int)hoop.y - 40, (int)(hoop.x + hoop.width + 5), (int)hoop.y + 20);
            } else {
                g2.drawLine((int)(hoop.x - 5), (int)hoop.y - 40, (int)(hoop.x - 5), (int)hoop.y + 20);
            }

            // --- DRAW NET ---
            g2.setColor(new Color(255, 255, 255, 120));
            g2.setStroke(new BasicStroke(1.5f));
            int netH = 45, bWidth = hoop.width - 20, offset = 10;
            for (int i = 0; i <= 4; i++) {
                int sX = (int)hoop.x + (i * hoop.width / 4);
                int eX = (int)hoop.x + offset + (i * bWidth / 4);
                g2.drawLine(sX, (int)hoop.y, eX, (int)hoop.y + netH);
            }
            g2.drawLine((int)hoop.x + 3, (int)hoop.y + 15, (int)(hoop.x + hoop.width - 3), (int)hoop.y + 15);
            g2.drawLine((int)hoop.x + 7, (int)hoop.y + 30, (int)(hoop.x + hoop.width - 7), (int)hoop.y + 30);

            // --- DRAW HOOP RIM ---
            g2.setColor(new Color(255, 60, 0));
            g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine((int)hoop.x, (int)hoop.y, (int)(hoop.x + hoop.width), (int)hoop.y);
            
            // --- DRAW BALL ---
            g2.setColor(new Color(240, 110, 40));
            g2.fillOval((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);

            // --- UI ---
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Verdana", Font.BOLD, 22));
            g2.drawString("Score: " + scoreManager.getScore(), 30, 45);
            if (scoreManager.getStreak() > 0) {
                g2.setColor(new Color(255, 200, 0));
                g2.drawString("STREAK x" + scoreManager.getStreak() + " 🔥", 30, 80);
            }

            if (gameState == 2) drawGameOverScreen(g2);
        }
    }

    private void drawHomeScreen(Graphics2D g) {
        g.setFont(new Font("Impact", Font.ITALIC, 70));
        g.setColor(new Color(255, 60, 0, 100));
        String t = "TAP TAP SHOOT";
        int tx = (getWidth() - g.getFontMetrics().stringWidth(t)) / 2;
        g.drawString(t, tx + 4, 204); 
        g.setColor(Color.WHITE);
        g.drawString(t, tx, 200);

        g.setFont(new Font("Verdana", Font.PLAIN, 18));
        g.setColor(new Color(200, 200, 200));
        String[] lines = {"Tap SPACE to fly", "Bounce off walls and score!", "PRESS [SPACE] TO START"};
        int y = 280;
        for (String l : lines) {
            g.drawString(l, (getWidth() - g.getFontMetrics().stringWidth(l)) / 2, y);
            y += 35;
        }
    }

    private void drawGameOverScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        g.setFont(new Font("Impact", Font.PLAIN, 60));
        g.drawString("GAME OVER", (getWidth() - g.getFontMetrics().stringWidth("GAME OVER")) / 2, 250);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Verdana", Font.BOLD, 24));
        String s = "Final Score: " + scoreManager.getScore();
        g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2, 310);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Tap Tap Arena");
        f.add(new TapTapShoot());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
