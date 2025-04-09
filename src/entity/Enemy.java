package entity;

import main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy extends Entity {
    protected GamePanel gp;

    // === Stats ===
    public int maxHP;
    public int currentHP;
    public int attack;
    public int speed;
    public boolean alive = true;

    // === Animation ===
    protected BufferedImage[][] walkFrames;
    protected BufferedImage[][] attackFrames;
    protected int walkFrameIndex = 0, walkCounter = 0;
    protected int attackFrameIndex = 0, attackTimer = 0;
    protected boolean isAttacking = false;
    protected boolean alreadyHitPlayer = false;
    protected int directionNum = 0;

    // === AI Movement ===
    private int actionLockCounter = 0;

    public Enemy(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(8, 16, 60, 60);
    }

    public void takeDamage(int dmg) {
        currentHP -= dmg;
        if (currentHP <= 0) {
            currentHP = 0;
            alive = false;
        }
    }

    public void update() {
        if (!alive) return;

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
        int slashX = worldX;
        int slashY = worldY;
        int slashW = 50;
        int slashH = 50;

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
            gp.player.takeDamage(attack);
            gp.player.worldX += (direction.equals("left") ? -10 : direction.equals("right") ? 10 : 0);
            gp.player.worldY += (direction.equals("up") ? -10 : direction.equals("down") ? 10 : 0);
        }

        if (gp.DEBUG_MODE) {
            Graphics2D g2 = (Graphics2D) gp.getGraphics();
            g2.setColor(Color.YELLOW);
            g2.drawRect(
                    slashArea.x - gp.player.worldX + gp.player.screenX,
                    slashArea.y - gp.player.worldY + gp.player.screenY,
                    slashArea.width, slashArea.height
            );
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

        BufferedImage image = isAttacking
                ? attackFrames[directionNum][attackFrameIndex]
                : walkFrames[directionNum][walkFrameIndex];

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, null);

        int barWidth = currentHP * 10;
        int barX = screenX + 32 - barWidth / 2;
        int barY = screenY - 10;

        g2.setColor(Color.RED);
        g2.fillRect(barX, barY, barWidth, 5);
    }

    // === Abstract methods for subclasses to override ===
    protected abstract void loadSprites();
    protected abstract int getWalkFrameLength();
    protected abstract int getAttackFrameLength();
    protected abstract int getAttackFrameToHit();
    protected abstract int getAttackSpeed();
}
