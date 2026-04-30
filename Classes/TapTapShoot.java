private void drawGameObjects(Graphics2D g2) {
    // --- 1. THE NET (Diamond Mesh Pattern) ---
    g2.setColor(new Color(255, 255, 255, 130));
    g2.setStroke(new BasicStroke(1.2f));
    int segments = 8;
    
    for (int i = 0; i <= segments; i++) {
        int topX = (int)hoop.x + (i * hoop.width / segments);
        int botX = (int)hoop.x + (hoop.width/4) + (i * (hoop.width/2) / segments);
        
        // Vertical swaying lines
        g2.drawLine(topX, (int)hoop.y, botX, (int)hoop.y + hoop.netHeight);
        
        // Criss-cross mesh lines for realism
        if (i < segments) {
            int nextBotX = (int)hoop.x + (hoop.width/4) + ((i+1) * (hoop.width/2) / segments);
            g2.drawLine(topX, (int)hoop.y, nextBotX, (int)hoop.y + (hoop.netHeight / 2));
        }
    }

    // --- 2. THE BALL ---
    g2.setColor(new Color(230, 90, 30));
    g2.fillOval((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2);
    
    // Ball texture (Simple ribs)
    g2.setColor(new Color(0, 0, 0, 80));
    g2.setStroke(new BasicStroke(2f));
    g2.drawArc((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2, 45, 180);
    g2.drawArc((int)ball.x, (int)ball.y, ball.radius*2, ball.radius*2, 45, -180);

    // --- 3. THE RIM (3D Ellipse) ---
    // Rim Shadow/Depth
    g2.setColor(new Color(150, 20, 0)); 
    g2.setStroke(new BasicStroke(hoop.thickness + 2));
    g2.drawOval((int)hoop.x, (int)hoop.y - 4, hoop.width, 12);

    // Main Rim
    g2.setColor(new Color(255, 60, 0));
    g2.setStroke(new BasicStroke(hoop.thickness));
    g2.drawOval((int)hoop.x, (int)hoop.y - 5, hoop.width, 10);
    
    // Small "attachment" piece to the wall
    g2.setColor(Color.GRAY);
    if (hoop.isOnRight) {
        g2.fillRect((int)hoop.x + hoop.width, (int)hoop.y - 2, 60, 4);
    } else {
        g2.fillRect(0, (int)hoop.y - 2, (int)hoop.x, 4);
    }
}
