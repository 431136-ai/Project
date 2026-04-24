import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TapTapShoot extends JPanel implements ActionListener {
    private Ball ball = new Ball();
    // Default to 800, but will update instantly when window opens
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
                    if (gameState == 0) {
                        hoop.reset(getWidth()); // Set hoop to actual screen size
                        gameState = 1;
                    } else if (gameState == 1) {
                        ball.jump();
                    }
                }
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (gameState == 1) {
            // Constantly update hoop position in case user resizes window
            hoop.updateResize(getWidth()); 
            
            // Pass the dynamic height and width so the floor works in fullscreen
            physics.applyPhysics(ball, getWidth(), getHeight(), hoop);

            if (physics.checkScore(ball, hoop)) {
                scoreManager.scoreBasket(!ball.hitRim);
                hoop.reset(getWidth()); 
                ball.hitRim = false; 
            }
        }
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
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

            // --- DRAW FLOOR VISUAL ---
            g2.setColor(new Color(40, 45, 60));
            g2.fillRect(0, getHeight() - 10, getWidth(), 10);

            // --- UI ---
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Verdana", Font.BOLD, 22));
            g2.drawString("Score: " + scoreManager.getScore(), 30, 45);
            if (scoreManager.getStreak() > 0) {
                g2.setColor(new Color(255, 200, 0));
                g2.drawString("STREAK x" + scoreManager.getStreak() + " 🔥", 30, 80);
            }
        }
    }

    private void drawHomeScreen(Graphics2D g) {
        g.setFont(new Font("Impact", Font.ITALIC, 70));
        g.setColor(new Color(255, 60, 0, 100));
        String t = "TAP TAP SHOOT";
        int tx = (getWidth() - g.getFontMetrics().stringWidth(t)) / 2;
        g.drawString(t, tx + 4, getHeight()/2 - 50); 
        g.setColor(Color.WHITE);
        g.drawString(t, tx, getHeight()/2 - 54);

        g.setFont(new Font("Verdana", Font.PLAIN, 18));
        g.setColor(new Color(200, 200, 200));
        String[] lines = {"Tap SPACE to fly", "Bounce off the floor and score!", "PRESS [SPACE] TO START"};
        int y = getHeight()/2 + 30;
        for (String l : lines) {
            g.drawString(l, (getWidth() - g.getFontMetrics().stringWidth(l)) / 2, y);
            y += 35;
        }
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Tap Tap Arena Pro");
        f.add(new TapTapShoot());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Made it resizable so you can test fullscreen!
        f.setResizable(true); 
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
