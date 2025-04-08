package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Goblin extends Enemy {

    private boolean alreadyHitPlayer = false;

    // === Animation Setup ===
    private final int walkRows = 4, walkCols = 4;
    private final int attackRows = 4, attackCols = 4;
    private final int frameWidth = 64, frameHeight = 64;

    // === Movement AI ===
    private int actionLockCounter = 0;

    public Goblin(GamePanel gp, int worldX, int worldY) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;

        speed = 1;
        maxHP = 2;
        currentHP = 2;
        attack = 1;

        direction = "down";
        directionNum = 0;

        loadWalkFrames();
        loadAttackFrames();
    }

    private void loadWalkFrames() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResourceAsStream("/enemies/gob1_walk.png")
            );
            UtilityTool uTool = new UtilityTool();

            walkFrames = new BufferedImage[walkRows][walkCols];
            for (int row = 0; row < walkRows; row++) {
                for (int col = 0; col < walkCols; col++) {
                    BufferedImage sub = sheet.getSubimage(
                            col * frameWidth, row * frameHeight,
                            frameWidth, frameHeight
                    );
                    walkFrames[row][col] = uTool.scaleImage(sub, gp.tileSize * 2, gp.tileSize * 2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAttackFrames() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResourceAsStream("/enemies/gob1_attack.png")
            );
            UtilityTool uTool = new UtilityTool();

            attackFrames = new BufferedImage[attackRows][attackCols];
            for (int row = 0; row < attackRows; row++) {
                for (int col = 0; col < attackCols; col++) {
                    BufferedImage sub = sheet.getSubimage(
                            col * frameWidth, row * frameHeight,
                            frameWidth, frameHeight
                    );
                    attackFrames[row][col] = uTool.scaleImage(sub, gp.tileSize * 2, gp.tileSize * 2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (!alive) return;

        if (isAttacking) {
            handleAttack();
            return;
        }

        handleRandomMovement();
        handleWalkingAnimation();

        if (isPlayerInRange()) {
            startAttack();
        }
    }

    private void handleAttack() {
        attackTimer++;

        if (attackFrameIndex == 2 && !alreadyHitPlayer) {
            checkHitPlayer();
            alreadyHitPlayer = true;
        }

        if (attackTimer > 10) {
            attackFrameIndex++;
            attackTimer = 0;

            if (attackFrameIndex >= attackCols) {
                attackFrameIndex = 0;
                isAttacking = false;
                alreadyHitPlayer = false;
            }
        }
    }

    private void checkHitPlayer() {
        int slashX = worldX;
        int slashY = worldY;
        int slashW = 20;
        int slashH = 20;

        // Slash box in direction
        switch (direction) {
            case "down" -> slashY += 40;
            case "up" -> slashY -= 40;
            case "left" -> slashX -= 40;
            case "right" -> slashX += 40;
        }

        Rectangle slashArea = new Rectangle(slashX, slashY, slashW, slashH);

        Rectangle playerArea = new Rectangle(
                gp.player.worldX + gp.player.solidArea.x,
                gp.player.worldY + gp.player.solidArea.y,
                gp.player.solidArea.width,
                gp.player.solidArea.height
        );

        if (slashArea.intersects(playerArea)) {
            gp.player.takeDamage(attack);
        }
    }

    private boolean isPlayerInRange() {
        int distX = Math.abs(worldX - gp.player.worldX);
        int distY = Math.abs(worldY - gp.player.worldY);
        return distX < 48 && distY < 48; // approx 1 tile range
    }

    private void startAttack() {
        isAttacking = true;
        attackFrameIndex = 0;
        attackTimer = 0;
    }

    private void handleRandomMovement() {
        actionLockCounter++;
        if (actionLockCounter > 120) {
            actionLockCounter = 0;
            int r = (int) (Math.random() * 4);
            switch (r) {
                case 0 -> { direction = "down";  directionNum = 0; }
                case 1 -> { direction = "up";    directionNum = 1; }
                case 2 -> { direction = "left";  directionNum = 2; }
                case 3 -> { direction = "right"; directionNum = 3; }
            }
        }

        // Check for tile collision
        collisionOn = false;
        gp.cChecker.checkTile(this);

        if (!collisionOn) {
            switch (direction) {
                case "down" -> worldY += speed;
                case "up" -> worldY -= speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }
    }

    private void handleWalkingAnimation() {
        walkCounter++;
        if (walkCounter > 15) {
            walkFrameIndex = (walkFrameIndex + 1) % walkCols;
            walkCounter = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!alive) return;

        BufferedImage image = isAttacking
                ? attackFrames[directionNum][attackFrameIndex]
                : walkFrames[directionNum][walkFrameIndex];

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, null);

        // Draw health bar above sprite
        int barWidth = currentHP * 10;
        int barX = screenX + 32 - barWidth / 2;
        int barY = screenY - 10;

        g2.setColor(Color.RED);
        g2.fillRect(barX, barY, barWidth, 5);
    }
}
