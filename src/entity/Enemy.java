package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Entity {
    protected GamePanel gp;

    public int maxHP, currentHP, attackDamage, speed;
    public boolean alive = true;

    protected BufferedImage[][] walkFrames, attackFrames, hurtFrames, deathFrames;
    protected int walkFrameIndex = 0, walkCounter = 0;
    protected int attackFrameIndex = 0, attackTimer = 0;
    protected boolean isAttacking = false, alreadyHitPlayer = false;
    protected boolean isHurt = false, isDying = false;
    protected int hurtTimer = 0, deathTimer = 0;
    protected int directionNum = 0;

    private int actionLockCounter = 0;

    public Enemy(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(8, 16, 60, 60);
    }

    public void takeDamage(int damage) {
        currentHP -= damage;
        triggerHurt();
        if (currentHP <= 0) {
            currentHP = 0;
            alive = false;
            triggerDeath();
        }
    }

    public int getDamage() {
        return attackDamage;
    }

    public void triggerHurt() {
        isHurt = true;
        hurtTimer = 0;
    }

    public void triggerDeath() {
        isDying = true;
        deathTimer = 0;
    }

    public void update() {
        if (!alive) return;

        if (isDying) {
            deathTimer++;
            if (deathTimer > 60) alive = false;
            return;
        }

        if (isHurt) {
            hurtTimer++;
            if (hurtTimer > 30) isHurt = false;
            return;
        }

        if (isAttacking) {
            handleAttack();
        } else {
            handleRandomMovement();
            handleWalkingAnimation();
            if (isPlayerInRange()) startAttack();
        }
    }

    protected void handleAttack() {
        attackTimer++;
        if (attackFrameIndex == getAttackFrameToHit() && !alreadyHitPlayer) {
            checkHitPlayer();
            alreadyHitPlayer = true;
        }

        if (attackTimer > getAttackSpeed()) {
            attackFrameIndex++;
            attackTimer = 0;
            if (attackFrameIndex >= getAttackFrameLength()) {
                attackFrameIndex = 0;
                isAttacking = false;
                alreadyHitPlayer = false;
            }
        }
    }

    protected void checkHitPlayer() {
        int slashX = worldX, slashY = worldY, slashW = 50, slashH = 50;
        switch (direction) {
            case "down" -> slashY += solidArea.height;
            case "up"   -> slashY -= slashH;
            case "left" -> slashX -= slashW;
            case "right"-> slashX += solidArea.width;
        }

        Rectangle slashArea = new Rectangle(slashX, slashY, slashW, slashH);
        Rectangle playerArea = new Rectangle(
                gp.player.worldX + gp.player.solidArea.x,
                gp.player.worldY + gp.player.solidArea.y,
                gp.player.solidArea.width,
                gp.player.solidArea.height
        );

        if (slashArea.intersects(playerArea)) {
            gp.player.takeDamage(getDamage());
        }
    }

    protected boolean isPlayerInRange() {
        int dx = Math.abs(worldX - gp.player.worldX);
        int dy = Math.abs(worldY - gp.player.worldY);
        return dx < 48 && dy < 48;
    }

    protected void startAttack() {
        isAttacking = true;
        attackFrameIndex = 0;
        attackTimer = 0;
    }

    protected void handleRandomMovement() {
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

    protected void handleWalkingAnimation() {
        walkCounter++;
        if (walkCounter > 15) {
            walkFrameIndex = (walkFrameIndex + 1) % getWalkFrameLength();
            walkCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        if (!alive) return;

        BufferedImage image = null;

        try {
            if (isDying && deathFrames.length > 0 && deathFrames[directionNum].length > 0) {
                image = deathFrames[directionNum][Math.min(deathTimer / 10, deathFrames[directionNum].length - 1)];
            } else if (isHurt && hurtFrames.length > 0 && hurtFrames[directionNum].length > 0) {
                image = hurtFrames[directionNum][Math.min(hurtTimer / 5, hurtFrames[directionNum].length - 1)];
            } else if (isAttacking && attackFrames.length > 0 && attackFrames[directionNum].length > 0) {
                image = attackFrames[directionNum][attackFrameIndex % attackFrames[directionNum].length];
            } else if (walkFrames.length > 0 && walkFrames[directionNum].length > 0) {
                image = walkFrames[directionNum][walkFrameIndex % walkFrames[directionNum].length];
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to get enemy frame at direction=" + directionNum + ": " + e.getMessage());
            e.printStackTrace();
        }

        if (image == null) return; // Skip drawing if image failed to load

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, null);

        g2.setColor(Color.RED);
        g2.fillRect(screenX + 32 - currentHP * 5, screenY - 10, currentHP * 10, 5);
    }




    protected void loadHurtFrames(String path) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream(path));
            UtilityTool u = new UtilityTool();
            int frameW = 64, frameH = 64;
            hurtFrames = new BufferedImage[4][6];
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 6; c++) {
                    BufferedImage sub = sheet.getSubimage(c * frameW, r * frameH, frameW, frameH);
                    hurtFrames[r][c] = u.scaleImage(sub, gp.tileSize * 2, gp.tileSize * 2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void loadDeathFrames(String path) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream(path));
            UtilityTool u = new UtilityTool();
            int frameW = 64, frameH = 64;
            deathFrames = new BufferedImage[4][6];
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 6; c++) {
                    BufferedImage sub = sheet.getSubimage(c * frameW, r * frameH, frameW, frameH);
                    deathFrames[r][c] = u.scaleImage(sub, gp.tileSize * 2, gp.tileSize * 2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected BufferedImage[][] loadSpriteSheet(String path, int scale, int cols, int rows) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream(path));
            BufferedImage[][] frames = new BufferedImage[rows][cols];
//            if (sheet.getWidth() == 0 || sheet.getHeight() == 0) {
//                System.err.println("❌ Warning: Loaded sprite has 0 size for " + path);
//            }

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
            return new BufferedImage[0][0]; // Return safe fallback to avoid crashing
        }
    }



    protected abstract void loadSprites(String walkPath, String attackPath);

    // === Abstracts ===
    protected abstract int getWalkFrameLength();
    protected abstract int getAttackFrameLength();
    protected abstract int getAttackFrameToHit();
    protected abstract int getAttackSpeed();
}
