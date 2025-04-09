package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
    public BufferedImage[][] hurtFrames;
    public BufferedImage[][] deathFrames;

    public int hurtTimer = 0;
    public int deathTimer = 0;
    public boolean isHurt = false;
    public boolean isDying = false;

    public PlayerAttack attackHandler;
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

        loadHurtFrames("/player/orc_hurt.png");
        loadDeathFrames("/player/orc_death.png");
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 2;
        worldY = gp.tileSize * 27;
        speed = 4;
        direction = "down";
        currentHP = maxHP;
    }

    public void update() {
        if (isDying) {
            handleDeathAnimation();
            return;
        }
        if (isHurt) {
            handleHurtAnimation();
            return;
        }
        if (attackHandler.updateAttack()) return;

        boolean moving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;
        if (moving) handleMovement();
        else frameIndex = 0;
    }

    private void handleMovement() {
        if (keyH.upPressed)      { direction = "up";    directionNum = 1; }
        else if (keyH.downPressed) { direction = "down";  directionNum = 0; }
        else if (keyH.leftPressed) { direction = "left";  directionNum = 2; }
        else if (keyH.rightPressed){ direction = "right"; directionNum = 3; }

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
        BufferedImage image;

        if (isDying) {
            image = deathFrames[directionNum][Math.min(deathTimer / 10, deathFrames[directionNum].length - 1)];
        } else if (isHurt) {
            image = hurtFrames[directionNum][Math.min(hurtTimer / 5, hurtFrames[directionNum].length - 1)];
        } else {
            image = animator.getCurrentFrame();
        }

        g2.drawImage(image, screenX, screenY, null);
    }


    public void takeDamage(int damage) {
        currentHP -= damage;
        triggerHurt();

        if (currentHP <= 0) {
            triggerDeath();
            currentHP = 0;
        }

        int knockback = gp.tileSize / 2;
        switch (direction) {
            case "up" -> worldY += knockback;
            case "down" -> worldY -= knockback;
            case "left" -> worldX += knockback;
            case "right" -> worldX -= knockback;
        }
    }

    private void handleHurtAnimation() {
        hurtTimer++;
        if (hurtTimer >= 30) isHurt = false;
    }

    private void handleDeathAnimation() {
        deathTimer++;
        if (deathTimer >= 60) {
            isDying = false;
            gp.gameState = GamePanel.DEATH_STATE;
        }
    }

    public void triggerHurt() {
        isHurt = true;
        hurtTimer = 0;
    }

    public void triggerDeath() {
        isDying = true;
        deathTimer = 0;
    }

    protected void loadHurtFrames(String path) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream(path));
            UtilityTool u = new UtilityTool();
            int frameW = 64, frameH = 64;

            hurtFrames = new BufferedImage[4][6]; // 4 rows, 6 columns

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

            deathFrames = new BufferedImage[4][6]; // 4 rows, 6 columns

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

    public boolean isDying() {
        return isDying;
    }

    public float cooldownRemaining(long startTime, int cooldownMs) {
        long now = System.currentTimeMillis();
        long timePassed = now - startTime;
        return Math.max(0, (cooldownMs - timePassed) / 1000f);
    }
}
