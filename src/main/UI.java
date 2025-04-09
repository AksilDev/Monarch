package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI {
    GamePanel gp;

    private BufferedImage heart_full;
    private BufferedImage heart_half;
    private BufferedImage heart_blank;

    public UI(GamePanel gp) {
        this.gp = gp;
        loadHUDGraphics();
    }

    // === Load heart images from /objects ===
    public void loadHUDGraphics() {
        try {
            heart_full  = ImageIO.read(getClass().getResourceAsStream("/objects/full.png"));
            heart_half  = ImageIO.read(getClass().getResourceAsStream("/objects/half.png"));
            heart_blank = ImageIO.read(getClass().getResourceAsStream("/objects/blank.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // === Called every frame in PLAY_STATE and WIN_STATE ===
    public void draw(Graphics2D g2) {
        drawPlayerLife(g2);
    }

    // === Draws hearts on top-left of screen ===
    private void drawPlayerLife(Graphics2D g2) {
        int x = 20;
        int y = 20;

        int hp = gp.player.currentHP;
        int max = gp.player.maxHP;

        int totalHearts = max / 2;

        for (int i = 0; i < totalHearts; i++) {
            if (hp >= 2) {
                g2.drawImage(heart_full, x, y, null);
                hp -= 2;
            } else if (hp == 1) {
                g2.drawImage(heart_half, x, y, null);
                hp--;
            } else {
                g2.drawImage(heart_blank, x, y, null);
            }
            x += 40; // space between hearts
        }
    }

    // === Draws "YOU DIED" overlay in DEATH_STATE ===
    public void drawDeathScreen(Graphics2D g2) {
        // Transparent black overlay
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // "YOU DIED" text
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        g2.setColor(Color.RED);
        String text = "YOU DIED";

        int textWidth = g2.getFontMetrics().stringWidth(text);
        int x = gp.screenWidth / 2 - textWidth / 2;
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }
}
