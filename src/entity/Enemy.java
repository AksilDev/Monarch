package entity;

import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    protected GamePanel gp;

    // === Stats ===
    public int maxHP;
    public int currentHP;
    public int attack;
    public boolean alive = true;

    // === Animation ===
    protected BufferedImage[][] walkFrames;
    protected BufferedImage[][] attackFrames;

    protected int walkFrameIndex = 0;
    protected int walkCounter = 0;

    protected int attackFrameIndex = 0;
    protected int attackTimer = 0;
    protected boolean isAttacking = false;

    protected int directionNum = 0;  // 0=down, 1=up, 2=left, 3=right

    public Enemy(GamePanel gp) {
        this.gp = gp;
        solidArea = new Rectangle(8, 16, 60, 60);
    }

    public void takeDamage(int dmg) {
        currentHP -= dmg;
        if (currentHP < 0) currentHP = 0;
        if (currentHP == 0) alive = false;
    }

    public void update() {
        // To be overridden by each enemy (e.g. Goblin, General, Boss)
    }

    public void draw(Graphics2D g2) {
        // To be overridden by each enemy
    }
}
