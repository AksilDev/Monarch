package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Goblin extends Enemy {

    boolean alreadyHitPlayer;
    private int walkRows = 4, walkCols = 4;   // Example: if gob1_walk is 4 rows x 4 cols
    private int attackRows = 4, attackCols = 4; // Example: if gob1_attack is also 4x4
    private int frameWidth = 64, frameHeight = 64; // Adjust to your sprite size

    private int directionNum; // 0=down,1=up,2=left,3=right (same idea as Player)

    // AI or movement variables
    private int actionLockCounter = 0;

    public Goblin(GamePanel gp, int worldX, int worldY) {
        super(gp);
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = 1;    // slower than player maybe
        this.maxHP = 2;
        this.currentHP = 2;
        this.attack = 1;   // damage to player
        loadWalkFrames();
        loadAttackFrames();
        direction = "down";
        directionNum = 0;
    }

    private void checkHitPlayer() {
        // Build a small slash area in front of goblin
        int slashX = worldX;
        int slashY = worldY;
        int slashW = 20;
        int slashH = 20;

        switch(direction) {
            case "down": slashY += 40; break;
            case "up":   slashY -= 40; break;
            case "left": slashX -= 40; break;
            case "right":slashX += 40; break;
        }

        Rectangle slashArea = new Rectangle(slashX, slashY, slashW, slashH);

        // Build player's bounding box
        Rectangle playerArea = new Rectangle(
                gp.player.worldX + gp.player.solidArea.x,
                gp.player.worldY + gp.player.solidArea.y,
                gp.player.solidArea.width,
                gp.player.solidArea.height
        );

        if(slashArea.intersects(playerArea)) {
            // do damage
            gp.player.takeDamage(attack);
        }
    }


    private void loadWalkFrames() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResourceAsStream("/enemies/gob1_walk.png")
            );
            UtilityTool uTool = new UtilityTool();

            walkFrames = new BufferedImage[walkRows][walkCols];
            for(int row=0; row<walkRows; row++){
                for(int col=0; col<walkCols; col++){
                    BufferedImage sub = sheet.getSubimage(
                            col*frameWidth, row*frameHeight, frameWidth, frameHeight
                    );
                    // scale if desired
                    sub = uTool.scaleImage(sub, gp.tileSize*2, gp.tileSize*2);
                    walkFrames[row][col] = sub;
                }
            }
        } catch(IOException e){
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
            for(int row=0; row<attackRows; row++){
                for(int col=0; col<attackCols; col++){
                    BufferedImage sub = sheet.getSubimage(
                            col*frameWidth, row*frameHeight, frameWidth, frameHeight
                    );
                    sub = uTool.scaleImage(sub, gp.tileSize*2, gp.tileSize*2);
                    attackFrames[row][col] = sub;
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        // If dead, do nothing
        if(!alive) {
            return;
        }

        // If attacking => do slash frames
        if (isAttacking) {
            // increment the timer
            attackTimer++;

            // if you want the goblin to hit the player at a specific frame, do:
            if (attackFrameIndex == 2 && !alreadyHitPlayer) {
                checkHitPlayer();
                alreadyHitPlayer = true;
            }

            // then the normal check
            if (attackTimer > 10) {
                attackFrameIndex++;
                attackTimer = 0;
                if (attackFrameIndex >= attackCols) {
                    attackFrameIndex = 0;
                    isAttacking = false;
                    alreadyHitPlayer = false;
                }
            }
            return;
        }

        // Otherwise, normal walk logic
        actionLockCounter++;
        if(actionLockCounter > 120) {
            actionLockCounter=0;
            int r = (int)(Math.random()*100) % 4;
            switch(r) {
                case 0: direction="down"; directionNum=0; break;
                case 1: direction="up";   directionNum=1; break;
                case 2: direction="left"; directionNum=2; break;
                case 3: direction="right";directionNum=3; break;
            }
        }

        collisionOn = false;
        gp.cChecker.checkTile(this);
        if(!collisionOn) {
            switch(direction) {
                case "down": worldY += speed; break;
                case "up":   worldY -= speed; break;
                case "left": worldX -= speed; break;
                case "right":worldX += speed; break;
            }
        }

        // Animate walk frames
        walkCounter++;
        if(walkCounter>15) {
            walkFrameIndex++;
            if(walkFrameIndex>=walkCols) walkFrameIndex=0;
            walkCounter=0;
        }

        // If close to player => attack
        if(isPlayerInRange()) {
            startAttack();
        }
    }


    private boolean isPlayerInRange() {
        // simple distance check
        int distX = Math.abs(worldX - gp.player.worldX);
        int distY = Math.abs(worldY - gp.player.worldY);
        return (distX < 48 && distY < 48); // if within 48px, start attacking
    }

    private void startAttack() {
        isAttacking=true;
        attackFrameIndex=0;
        attackTimer=0;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(!alive) return; // skip drawing if dead

        BufferedImage image = null;

        if(isAttacking) {
            image = attackFrames[directionNum][attackFrameIndex];
        } else {
            image = walkFrames[directionNum][walkFrameIndex];
        }

        // Convert worldX/Y to screenX/Y
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        g2.drawImage(image, screenX, screenY, null);

        // center the bar
        int barWidth = currentHP * 10;
        int barX = screenX + (64/2) - (barWidth/2); // center over sprite
        int barY = screenY - 10;                    // 10 px above

        g2.fillRect(barX, barY, barWidth, 5);

    }
}
