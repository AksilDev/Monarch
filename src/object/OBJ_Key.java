package object;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Key extends SuperObject {

    private BufferedImage[] frames;
    private int frameIndex = 0;
    private int spriteCounter = 0;

    public OBJ_Key(GamePanel gp) {
        name = "Key";
        collision = false;
        loadFrames(gp);
    }

    private void loadFrames(GamePanel gp) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
            frames = new BufferedImage[4]; // 4 frames horizontally

            for (int i = 0; i < 4; i++) {
                BufferedImage sub = sheet.getSubimage(i * 16, 0, 16, 35);
                frames[i] = new UtilityTool().scaleImage(sub, gp.tileSize, gp.tileSize);
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
        g2.drawImage(frames[frameIndex], screenX, screenY, null);
    }
}
