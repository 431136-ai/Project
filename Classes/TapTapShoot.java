import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TapTapShoot extends JPanel implements ActionListener {
    private Ball ball = new Ball();
    private Hoop hoop = new Hoop(800);
    private Backboard backboard = new Backboard();
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
        scoreManager.reset();
        timeManager.reset();
        gameState = 1;
    }

    public void actionPerformed(ActionEvent e) {
        if (gameState == 1) {
            physics.applyPhysics(ball, getWidth(), hoop, backboard);
            timeManager.update();

            // Check if scored!
            if (physics.checkScore(ball, hoop)) {
                // If it never touched rim or board, it's a swish!
                boolean isSwish = !ball.hitRimOrBoard;
                scoreManager.scoreBasket(isSwish);
                
                timeManager.reset(); // Full time restore
                hoop.teleport(getWidth()); // Move hoop to new side
                
                // Reset ball state for the next shot
                ball.y = 50; 
                ball.velocityV = 0;
                ball.hitRimOrBoard = false; 
            }

            if (ball.y > getHeight() || timeManager.isTimeUp()) gameState = 2;
        }
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, getWidth(), getHeight());

        if (gameState == 0) {
            drawScreen(g2, "TAP TAP SHOOT", "Press SPACE to Play");
        } else {
            // Draw Ball
            g2.setColor(Color.ORANGE);
            g2.fillOval((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);

            // Draw Hoop (Gap in the middle, rims on the side)
            g2.setColor(Color.GRAY); // Left Rim
            Rectangle lRim = hoop.getLeftRim();
            g2.fillRect(lRim.x, lRim.y, lRim.width, lRim.height);
            
            g2.setColor(Color.GRAY); // Right Rim
            Rectangle rRim = hoop.getRightRim();
            g2.fillRect(rRim.x, rRim.y, rRim.width, rRim.height);

            // Draw Backboard
            Rectangle b = backboard.getBounds(hoop);
            g2.setColor(Color.WHITE);
            g2.fillRect(b.x, b.y, b.width, b.height);
            g2.setColor(Color.RED); // Little red square on backboard
            g2.fillRect(b.x, b.y + 30, b.width, 20);

            // Draw Time Bar
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(250, 20, 300, 15);
            g2.setColor(timeManager.timeLeft < 30 ? Color.RED : Color.GREEN);
            g2.fillRect(250, 20, (int)(3 * timeManager.timeLeft), 15);

            // Draw Score and Streak
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.drawString("Score: " + scoreManager.getScore(), 20, 35);
            
            if (scoreManager.getStreak() > 0) {
                g2.setColor(Color.ORANGE);
                g2.drawString("STREAK x" + scoreManager.getStreak() + " 🔥", 20, 65);
            }

            if (gameState == 2) drawScreen(g2, "GAME OVER", "Space to Restart");
        }
    }

    private void drawScreen(Graphics2D g, String main, String sub) {
        g.setColor(new Color(0,0,0,150));
        g.fillRect(0,0,800,600);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString(main, 250, 250);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString(sub, 300, 300);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Tap Tap Shoot");
        f.add(new TapTapShoot());
        f.setSize(800, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
    }
}
