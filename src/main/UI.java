package main;

import entity.PlayerAttack;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UI{
    GamePanel gp;

    private BufferedImage heart_full;
    private BufferedImage heart_half;
    private BufferedImage heart_blank;

    public UI(GamePanel gp) {
        this.gp = gp;
        loadHUDGraphics();
    }

    public void loadHUDGraphics() {
        try {
            heart_full = ImageIO.read(getClass().getResourceAsStream("/objects/full.png"));
            heart_half = ImageIO.read(getClass().getResourceAsStream("/objects/half.png"));
            heart_blank = ImageIO.read(getClass().getResourceAsStream("/objects/blank.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        drawPlayerLife(g2);
        if (gp.gameState == GamePanel.PLAY_STATE) drawSkillCooldowns(g2);
    }

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
            x += 40;
        }
    }

    public void drawDeathScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        g2.setColor(Color.RED);
        String text = "YOU DIED";

        int textWidth = g2.getFontMetrics().stringWidth(text);
        int x = gp.screenWidth / 2 - textWidth / 2;
        int y = gp.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public void drawSkillCooldowns(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        int x = 20;
        int y = gp.screenHeight - 40;

        float specialCD = gp.player.attackHandler.cooldownRemaining(gp.player.attackHandler.specialCooldownStart, 4000);
        float ultCD = gp.player.attackHandler.cooldownRemaining(gp.player.attackHandler.ultimateCooldownStart, 8000);

        String j = "[J] Basic";
        String k = "[K] Special: " + (specialCD <= 0 ? "Ready" : String.format("%.1fs", specialCD));
        String l = "[L] Ult: " + (ultCD <= 0 ? "Ready" : String.format("%.1fs", ultCD));

        g2.drawString(j, x, y);
        g2.drawString(k, x + 140, y);
        g2.drawString(l, x + 360, y);
    }

}