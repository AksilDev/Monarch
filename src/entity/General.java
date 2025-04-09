package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;

public class General extends Enemy {

    public General(GamePanel gp, int worldX, int worldY) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;

        maxHP = 6;
        currentHP = 6;
        attack = 4;
        speed = 1;
        direction = "down";
        directionNum = 0;

        solidArea = new Rectangle(8, 16, 60, 60); // larger?
        loadSprites();
    }

    @Override
    protected void loadSprites() {
        try {
            BufferedImage walkSheet = ImageIO.read(getClass().getResourceAsStream("/enemies/general_full.png"));
            BufferedImage attackSheet = ImageIO.read(getClass().getResourceAsStream("/enemies/general_attack.png"));
            UtilityTool uTool = new UtilityTool();

            int frameSize = 64;
            int scaled = gp.tileSize * 2 + 12;

            walkFrames = new BufferedImage[4][8];
            attackFrames = new BufferedImage[4][9];

            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 8; col++) {
                    walkFrames[row][col] = uTool.scaleImage(
                            walkSheet.getSubimage(col * frameSize, row * frameSize, frameSize, frameSize),
                            scaled, scaled
                    );
                }
                for (int col = 0; col < 9; col++) {
                    attackFrames[row][col] = uTool.scaleImage(
                            attackSheet.getSubimage(col * frameSize, row * frameSize, frameSize, frameSize),
                            scaled, scaled
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override protected int getWalkFrameLength() { return 8; }
    @Override protected int getAttackFrameLength() { return 9; }
    @Override protected int getAttackFrameToHit() { return 4; }
    @Override protected int getAttackSpeed() { return 15; }
}
