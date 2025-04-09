package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity {
    public int worldX, worldY;
    public int speed;
    public String direction = "down";
    public Rectangle solidArea;
    public boolean collisionOn = false;

    public boolean isHurt = false;
    public boolean isDying = false;
    protected int hurtTimer = 0;
    protected int deathTimer = 0;

    protected BufferedImage[][] hurtFrames;
    protected BufferedImage[][] deathFrames;

    public abstract void update();
    public abstract void draw(Graphics2D g2);

    public void triggerHurt() {
        isHurt = true;
        hurtTimer = 0;
    }

    public void triggerDeath() {
        isDying = true;
        deathTimer = 0;
    }
}