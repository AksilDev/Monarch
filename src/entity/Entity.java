package entity;

import java.awt.Rectangle;

public class Entity {
    public int worldX, worldY;           // Position in the world
    public int speed;                    // Movement speed
    public String direction;             // Current facing direction
    public int spriteNum = 1;            // Animation sprite index

    public Rectangle solidArea;          // Collision box
    public boolean collisionOn = false;  // Flag for tile collision
}
