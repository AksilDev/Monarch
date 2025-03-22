package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SuperObject {
    public String name;
    public BufferedImage image;
    public boolean collision = false;

    public int worldX, worldY;

    public void update() {}

    public void draw(Graphics2D g2, int screenX, int screenY, int tileSize) {
        if(image != null) {
            g2.drawImage(image, screenX, screenY, tileSize, tileSize, null);
        }
    }
}