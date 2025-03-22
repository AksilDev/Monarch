package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI {
    GamePanel gp;

    // Three images for each heart state
    BufferedImage heart_full;
    BufferedImage heart_half;
    BufferedImage heart_blank;

    public UI(GamePanel gp) {
        this.gp = gp;
        loadHUDGraphics();
    }

    public void loadHUDGraphics() {
        try {
            // Load each heart image
            heart_full  = ImageIO.read(getClass().getResourceAsStream("/objects/full.png"));
            heart_half  = ImageIO.read(getClass().getResourceAsStream("/objects/half.png"));
            heart_blank = ImageIO.read(getClass().getResourceAsStream("/objects/blank.png"));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        drawPlayerLife(g2);
    }

    private void drawPlayerLife(Graphics2D g2) {
        // Starting position on screen for the hearts
        int x = 20;
        int y = 20;

        int hp  = gp.player.currentHP; // e.g. 0..10
        int max = gp.player.maxHP;     // e.g. 10

        // The number of hearts to draw is maxHP / 2 (10/2=5 hearts)
        int totalHearts = max / 2;

        for(int i = 0; i < totalHearts; i++) {
            if(hp >= 2) {
                // Draw full heart
                g2.drawImage(heart_full, x, y, null);
                hp -= 2; // used two "half units"
            }
            else if(hp == 1) {
                // Draw half heart
                g2.drawImage(heart_half, x, y, null);
                hp -= 1; // used one "half unit"
            }
            else {
                g2.drawImage(heart_blank, x, y, null);
            }
            // Move x for the next heart
            x += 40;
        }
    }

    public void drawDeathScreen(Graphics2D g2) {
        // 1) Draw a semi-transparent black rectangle over the whole screen
        g2.setColor(new Color(0, 0, 0, 150)); // 150 alpha for transparency
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        //YOU DIED
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        g2.setColor(Color.red);
        String text = "YOU DIED";

        // Centering text
        int textWidth  = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int textHeight = (int)g2.getFontMetrics().getStringBounds(text, g2).getHeight();
        int x = gp.screenWidth/2 - textWidth/2;
        int y = gp.screenHeight/2 - textHeight/2;

        g2.drawString(text, x, y);
    }

}