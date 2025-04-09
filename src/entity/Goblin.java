package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Goblin extends Enemy {

    public Goblin(GamePanel gp, int worldX, int worldY) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;

        maxHP = 2;
        currentHP = 2;
        attack = 1;
        speed = 1;
        direction = "down";
        directionNum = 0;

        loadSprites();
    }

    @Override
    protected void loadSprites() {
        try {
            BufferedImage walkSheet = ImageIO.read(getClass().getResourceAsStream("/enemies/gob1_walk.png"));
            BufferedImage attackSheet = ImageIO.read(getClass().getResourceAsStream("/enemies/gob1_attack.png"));
            UtilityTool uTool = new UtilityTool();

            int frameWidth = 64, frameHeight = 64;
            int scaled = gp.tileSize * 2;

            walkFrames = new BufferedImage[4][4];
            attackFrames = new BufferedImage[4][4];

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    walkFrames[row][col] = uTool.scaleImage(
                            walkSheet.getSubimage(col * frameWidth, row * frameHeight, frameWidth, frameHeight),
                            scaled, scaled
                    );
                    attackFrames[row][col] = uTool.scaleImage(
                            attackSheet.getSubimage(col * frameWidth, row * frameHeight, frameWidth, frameHeight),
                            scaled, scaled
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override protected int getWalkFrameLength() { return 4; }
    @Override protected int getAttackFrameLength() { return 4; }
    @Override protected int getAttackFrameToHit() { return 2; }
    @Override protected int getAttackSpeed() { return 10; }
}
