package entity;

import main.GamePanel;
import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    protected GamePanel gp;

    // Basic stats
    public int maxHP;
    public int currentHP;
    public int attack;
    public boolean attacking;
    public boolean alive = true;

    // Walk frames
    protected BufferedImage[][] walkFrames;
    protected int walkFrameIndex;
    protected int walkCounter;

    // Attack frames
    protected BufferedImage[][] attackFrames;
    protected int attackFrameIndex;
    protected int attackTimer;
    protected boolean isAttacking;

    // For direction indexing (0=down, 1=up, 2=left, 3=right)
    protected int directionNum;

    public Enemy(GamePanel gp) {
        this.gp = gp;
        // default collision box
        solidArea = new java.awt.Rectangle(8,16,60,60);
    }

    public void takeDamage(int dmg) {
        currentHP -= dmg;
        if(currentHP < 0) currentHP = 0;
        if(currentHP == 0) {
            alive = false;
        }
    }

    public void update() {
        // Overridden by each enemy type (goblin, boss, etc.)
    }

    public void draw(java.awt.Graphics2D g2) {
        // Overridden by each enemy type
    }
}
