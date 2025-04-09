package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Goblin extends Enemy {

    public Goblin(GamePanel gp, int worldX, int worldY) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;

        maxHP = 2;
        currentHP = 2;
        attackDamage = 1;
        speed = 1;

        direction = "down";
        directionNum = 0;

        loadSprites("/enemies/gob1_walk.png", "/enemies/gob1_attack.png");
        // No hurt or death animations for goblin
    }

    @Override
    protected void loadSprites(String walkPath, String attackPath) {
        walkFrames = loadSpriteSheet(walkPath, gp.tileSize * 2, 6, 4);
        attackFrames = loadSpriteSheet(attackPath, gp.tileSize * 2, 5, 4); // 4x4 attack
    }


    protected BufferedImage[][] loadSpriteSheet(String path, int scale, int cols, int rows) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream(path));
            BufferedImage[][] frames = new BufferedImage[rows][cols];

            int frameW = sheet.getWidth() / cols;
            int frameH = sheet.getHeight() / rows;
            UtilityTool u = new UtilityTool();

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    BufferedImage sub = sheet.getSubimage(c * frameW, r * frameH, frameW, frameH);
                    frames[r][c] = u.scaleImage(sub, scale, scale);
                }
            }

            return frames;
        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage[0][0];
        }
    }

    @Override protected int getWalkFrameLength() { return 4; }
    @Override protected int getAttackFrameLength() { return 4; }
    @Override protected int getAttackFrameToHit() { return 2; }
    @Override protected int getAttackSpeed() { return 10; }
    @Override public int getDamage() { return attackDamage; }

    // Optional: override draw to skip hurt/death logic since they don’t exist
    @Override
    public void draw(Graphics2D g2) {
        if (!alive) return;

        BufferedImage image = isAttacking
                ? attackFrames[directionNum][attackFrameIndex % attackFrames[directionNum].length]
                : walkFrames[directionNum][walkFrameIndex % walkFrames[directionNum].length];

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, null);
        g2.setColor(Color.RED);
        g2.fillRect(screenX + 32 - currentHP * 5, screenY - 10, currentHP * 10, 5);
    }
}
