package object;

import main.GamePanel;
import main.UtilityTool;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends SuperObject {
    BufferedImage[] frames;
    int frameIndex, spriteCounter;

    public OBJ_Key(GamePanel gp) {
        name = "Key";
        collision = false;
        loadFrames(gp);
    }

    private void loadFrames(GamePanel gp) {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResourceAsStream("/objects/key.png")
            );
            // if ang 4 frames, each 16×16:
            frames = new BufferedImage[4];
            for(int i=0; i<4; i++){
                BufferedImage sub = sheet.getSubimage(i*16, 0, 16, 35);
                // scale if need
                sub = new UtilityTool().scaleImage(sub, gp.tileSize, gp.tileSize);
                frames[i] = sub;
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        spriteCounter++;
        if(spriteCounter > 10){
            frameIndex++;
            if(frameIndex >= frames.length) frameIndex=0;
            spriteCounter=0;
        }
    }

    @Override
    public void draw(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(frames[frameIndex], screenX, screenY, null);
    }
}