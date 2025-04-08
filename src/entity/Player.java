package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    // === Health ===
    public int maxHP = 10;
    public int currentHP = 10;

    // === Screen Position (fixed center) ===
    public final int screenX;
    public final int screenY;

    // === Movement ===
    private int spriteCounter = 0;
    private int frameIndex = 0;
    private int directionNum = 0;

    // === Walking Frames ===
    public BufferedImage[][] frames;

    // === Attack ===
    public boolean attacking = false;
    private BufferedImage[][] attackFrames;
    private int attackFrameIndex = 0;
    private int attackTimer = 0;
    private final int ATTACK_FRAMES_PER_DIRECTION = 8;
    private boolean alreadyHit = false;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle(8, 16, 60, 60);

        setDefaultValues();
        loadWalkFrames();
        loadAttackFrames();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 2;
        worldY = gp.tileSize * 27;
        speed = 4;
        direction = "down";
        currentHP = maxHP;
    }

    private void loadWalkFrames() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResourceAsStream("/player/orc2_full.png")
            );
            UtilityTool uTool = new UtilityTool();

            int rows = 4, cols = 8;
            int frameWidth = 64, frameHeight = 64;

            frames = new BufferedImage[rows][cols];

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    BufferedImage sub = sheet.getSubimage(
                            col * frameWidth, row * frameHeight,
                            frameWidth, frameHeight
                    );
                    frames[row][col] = uTool.scaleImage(sub, gp.tileSize * 2, gp.tileSize * 2);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAttackFrames() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResourceAsStream("/player/orc2_attack.png")
            );
            UtilityTool uTool = new UtilityTool();

            int rows = 4, cols = 8;
            int frameWidth = 64, frameHeight = 64;

            attackFrames = new BufferedImage[rows][cols];

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
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

    public void startAttack() {
        attacking = true;
        attackFrameIndex = 0;
        attackTimer = 0;
    }

    public void takeDamage(int damage) {
        currentHP -= damage;
        if (currentHP <= 0) {
            currentHP = 0;
            gp.gameState = GamePanel.DEATH_STATE;
        }
    }

//    @Override
    public void update() {
        if (attacking) {
            handleAttackFrames();
            return;
        }

        boolean moving = keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed;

        if (moving) {
            handleMovement();
        } else {
            frameIndex = 0;
        }

        if (currentHP <= 0) {
            currentHP = 0;
            gp.gameState = GamePanel.DEATH_STATE;
        }
    }

    private void handleMovement() {
        // Direction & animation frame
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

        // Collision check
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

        // Animation
        spriteCounter++;
        if (spriteCounter > 12) {
            frameIndex = (frameIndex + 1) % frames[directionNum].length;
            spriteCounter = 0;
        }
    }

    private void handleAttackFrames() {
        attackTimer++;

        if (attackFrameIndex == 2 && !alreadyHit) {
            checkHitEnemies();
            alreadyHit = true;
        }

        if (attackTimer > 2) {
            attackFrameIndex++;
            attackTimer = 0;

            if (attackFrameIndex >= ATTACK_FRAMES_PER_DIRECTION) {
                attackFrameIndex = 0;
                attacking = false;
                alreadyHit = false;
            }
        }
    }

    private void checkHitEnemies() {
        // Create a "slash zone" in front of player
        int slashX = worldX;
        int slashY = worldY;
        int slashW = 40;
        int slashH = 40;

        switch (direction) {
            case "down" -> slashY += 40;
            case "up" -> slashY -= 40;
            case "left" -> slashX -= 40;
            case "right" -> slashX += 40;
        }

        Rectangle slashArea = new Rectangle(slashX, slashY, slashW, slashH);

        for (var enemy : gp.enemies) {
            if (enemy != null && enemy.alive) {
                Rectangle enemyBox = new Rectangle(
                        enemy.worldX + enemy.solidArea.x,
                        enemy.worldY + enemy.solidArea.y,
                        enemy.solidArea.width,
                        enemy.solidArea.height
                );

                if (slashArea.intersects(enemyBox)) {
                    enemy.takeDamage(1);
                }
            }
        }
    }

//    @Override
    public void draw(Graphics2D g2) {
        BufferedImage image;

        if (attacking) {
            image = attackFrames[directionNum][attackFrameIndex];
        } else {
            image = frames[directionNum][frameIndex];
        }

        g2.drawImage(image, screenX, screenY, null);
    }
}
