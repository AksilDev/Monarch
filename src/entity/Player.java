package entity;

import main.GamePanel;
import main.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    public GamePanel gp;
    public KeyHandler keyH;

    public int maxHP = 10;
    public int currentHP = 10;

    public final int screenX;
    public final int screenY;

    public boolean attacking = false;
    public boolean usingSpecial = false;
    public boolean usingUltimate = false;

    public int attackFrameIndex = 0;
    public int attackTimer = 0;
    public final int ATTACK_FRAMES_PER_DIRECTION = 8;
    public boolean alreadyHit = false;

    public int frameIndex = 0;
    public int directionNum = 0;
    private int spriteCounter = 0;

    public long specialCooldownStart = 0;
    public long ultimateCooldownStart = 0;
    private final int SPECIAL_COOLDOWN_MS = 4000;
    private final int ULTIMATE_COOLDOWN_MS = 8000;

    public BufferedImage[][] frames;
    public BufferedImage[][] attackFrames;
    public BufferedImage[][] specialFrames;
    public BufferedImage[][] ultFrames;

    private PlayerAttack attackHandler;
    private PlayerAnimation animator;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 60, 60);

        setDefaultValues();

        animator = new PlayerAnimation(this);
        animator.loadAllFrames();

        attackHandler = new PlayerAttack(this);
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 2;
        worldY = gp.tileSize * 27;
        speed = 4;
        direction = "down";
        currentHP = maxHP;
    }

    public void update() {
        if (attackHandler.updateAttack()) return;

        boolean moving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;

        if (moving) {
            handleMovement();
        } else {
            frameIndex = 0;
        }
    }

    private void handleMovement() {
        if (keyH.upPressed) {
            direction = "up";
            directionNum = 1;
        } else if (keyH.downPressed) {
            direction = "down";
            directionNum = 0;
        } else if (keyH.leftPressed) {
            direction = "left";
            directionNum = 2;
        } else if (keyH.rightPressed) {
            direction = "right";
            directionNum = 3;
        }

        collisionOn = false;
        gp.cChecker.checkTile(this);
        if (!collisionOn) {
            switch (direction) {
                case "up" -> worldY -= speed;
                case "down" -> worldY += speed;
                case "left" -> worldX -= speed;
                case "right" -> worldX += speed;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            frameIndex = (frameIndex + 1) % frames[directionNum].length;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = animator.getCurrentFrame();
        g2.drawImage(image, screenX, screenY, null);
    }

    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP <= 0) {
            currentHP = 0;
            gp.gameState = GamePanel.DEATH_STATE;
        }

        int knockbackDistance = gp.tileSize / 2;
        switch (direction) {
            case "up" -> worldY += knockbackDistance;
            case "down" -> worldY -= knockbackDistance;
            case "left" -> worldX += knockbackDistance;
            case "right" -> worldX -= knockbackDistance;
        }
    }

    public void startAttack() {
        attacking = true;
        attackFrameIndex = 0;
        attackTimer = 0;
    }

    public void startSpecial() {
        if (!usingSpecial && isSpecialReady()) {
            usingSpecial = true;
            specialCooldownStart = System.currentTimeMillis();
            attackFrameIndex = 0;
            attackTimer = 0;
        }
    }

    public void startUltimate() {
        if (!usingUltimate && isUltimateReady()) {
            usingUltimate = true;
            ultimateCooldownStart = System.currentTimeMillis();
            attackFrameIndex = 0;
            attackTimer = 0;
        }
    }

    public boolean isSpecialReady() {
        return System.currentTimeMillis() - specialCooldownStart >= SPECIAL_COOLDOWN_MS;
    }

    public boolean isUltimateReady() {
        return System.currentTimeMillis() - ultimateCooldownStart >= ULTIMATE_COOLDOWN_MS;
    }

    public float cooldownRemaining(long startTime, int cooldownMs) {
        long now = System.currentTimeMillis();
        long timePassed = now - startTime;
        return Math.max(0, (cooldownMs - timePassed) / 1000f);
    }
}
