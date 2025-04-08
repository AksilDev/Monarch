package object;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Portal extends SuperObject {

    private BufferedImage[] frames;
    private int frameIndex = 0;
    private int spriteCounter = 0;

    public OBJ_Portal(GamePanel gp) {
        name = "Portal";
        collision = false;
        loadFrames(gp);
    }

    private void loadFrames(GamePanel gp) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/portal.png"));
            UtilityTool uTool = new UtilityTool();

            frames = new BufferedImage[6]; // 3 cols x 2 rows
            int frameWidth = 32, frameHeight = 32;
            int index = 0;

            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 3; col++) {
                    BufferedImage sub = sheet.getSubimage(col * frameWidth, row * frameHeight, frameWidth, frameHeight);
                    frames[index++] = uTool.scaleImage(sub, 80, 80);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        spriteCounter++;
        if (spriteCounter > 10) {
            frameIndex = (frameIndex + 1) % frames.length;
            spriteCounter = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2, int screenX, int screenY, int tileSize) {
        if (frames != null && frames.length > 0) {
            g2.drawImage(frames[frameIndex], screenX, screenY, null);
        }
    }
}
