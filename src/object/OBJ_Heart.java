package object;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OBJ_Heart extends SuperObject {
    public BufferedImage[] frames;
    private int frameIndex;
    private int spriteCounter;

    public OBJ_Heart(GamePanel gp) {
        name = "Heart";
        collision = false;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/heart1.png"));
            UtilityTool uTool = new UtilityTool();
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}