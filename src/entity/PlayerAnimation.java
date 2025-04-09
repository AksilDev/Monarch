package entity;

import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PlayerAnimation {
    private final Player player;

    public PlayerAnimation(Player player) {
        this.player = player;
    }

    public void loadAllFrames() {
        player.frames = loadFrames("/player/orc_full.png", 64, 64, player.gp.tileSize * 2);
        player.attackFrames = loadFrames("/player/orc_attack.png", 64, 64, player.gp.tileSize * 2);
        player.specialFrames = loadFrames("/player/orc_special.png", 64, 64, player.gp.tileSize * 2 + 8);
        player.ultFrames = loadFrames("/player/orc_ult.png", 64, 64, player.gp.tileSize * 2 + 16);
    }

    private BufferedImage[][] loadFrames(String path, int frameW, int frameH, int scale) {
        try {
            BufferedImage sheet = ImageIO.read(getClass().getResourceAsStream(path));
            UtilityTool uTool = new UtilityTool();
            int rows = 4;
            int cols = 8;
            BufferedImage[][] result = new BufferedImage[rows][cols];

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    BufferedImage sub = sheet.getSubimage(col * frameW, row * frameH, frameW, frameH);
                    result[row][col] = uTool.scaleImage(sub, scale, scale);
                }
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public BufferedImage getCurrentFrame() {
        if (player.attacking) {
            return player.attackFrames[player.directionNum][player.attackFrameIndex];
        }
        if (player.usingSpecial) {
            return player.specialFrames[player.directionNum][player.attackFrameIndex];
        }
        if (player.usingUltimate) {
            return player.ultFrames[player.directionNum][player.attackFrameIndex];
        }
        return player.frames[player.directionNum][player.frameIndex];
    }

}

