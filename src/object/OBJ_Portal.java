package object;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Portal extends SuperObject {

    public BufferedImage[] frames;
    private int frameIndex;
    private int spriteCounter;

    public OBJ_Portal(GamePanel gp) {
        name = "Portal";
        collision = false;
        loadFrames(gp);
    }

    private void loadFrames(GamePanel gp) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream("/objects/portal.png"));
            UtilityTool uTool = new UtilityTool();


            frames = new BufferedImage[6];
            int frameWidth = 32;
            int frameHeight = 32;
            int index = 0;
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 3; col++) {
                    BufferedImage sub = sheet.getSubimage(
                            col * frameWidth,
                            row * frameHeight,
                            frameWidth, frameHeight
                    );
                    sub = uTool.scaleImage(sub, 80, 80);
                    frames[index++] = sub;
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
            frameIndex++;
            if (frameIndex >= frames.length) {
                frameIndex = 0;
            }
            spriteCounter = 0;
        }
    }

    @Override
    public void draw(java.awt.Graphics2D g2, int screenX, int screenY, int tileSize) {
        if(frames != null && frames.length > 0) {
            g2.drawImage(frames[frameIndex], screenX, screenY, null);
        }
    }
}