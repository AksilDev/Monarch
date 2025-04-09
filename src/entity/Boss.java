package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Boss extends Enemy {

    public Boss(GamePanel gp, int x, int y) {
        super(gp);
        this.worldX = x;
        this.worldY = y;
        this.worldY = y;

        maxHP = 20;
        currentHP = maxHP;
        attackDamage = 4;
        speed = 1;

        direction = "down";
        directionNum = 0;

        loadSprites("/enemies/Demon_full.png", "/enemies/Demon_attack.png");
        hurtFrames = loadSpriteSheet("/enemies/Demon_hurt.png", gp.tileSize * 2 + 12, 4, 4);
        deathFrames = loadSpriteSheet("/enemies/Demon_death.png", gp.tileSize * 2 + 12, 13, 4);
    }


    @Override
    protected void loadSprites(String walkPath, String attackPath) {
        walkFrames = loadSpriteSheet(walkPath, gp.tileSize * 2 + 12, 6, 4);
        attackFrames = loadSpriteSheet(attackPath, gp.tileSize * 2 + 12, 6, 4);
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

    @Override protected int getWalkFrameLength()    { return 6; }
    @Override protected int getAttackFrameLength()  { return 6; }
    @Override protected int getAttackFrameToHit()   { return 3; }
    @Override protected int getAttackSpeed()        { return 6; }
    @Override public int getDamage()                { return attackDamage; }
}
