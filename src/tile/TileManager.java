package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[30]; // Increase if needed
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        loadTileImages();
    }

    public void loadTileImages() {
        // === World 1 Tiles (0–9) ===
        setup(0, "tiles/1", false);
        setup(1, "tiles/10", true);
        setup(2, "tiles/2", true);
        setup(3, "tiles/3", false);
        setup(4, "tiles/4", true);
        setup(5, "tiles/5", true);
        setup(6, "tiles/6", false);
        setup(7, "tiles/7", false);
        setup(8, "tiles/8", false);
        setup(9, "tiles/9", false);

        // === World 2 Tiles (10–20) ===
        setup(10, "tiles2/Floor1", false);
        setup(11, "tiles2/Floor2", false);
        setup(12, "tiles2/grass1", true);
        setup(13, "tiles2/grass2", true);
        setup(14, "tiles2/wall1", false);
        setup(15, "tiles2/wall2", true);
        setup(16, "tiles2/cliff1", true);
        setup(17, "tiles2/cliff2", true);
        setup(18, "tiles2/Floor3", false);
        setup(19, "tiles2/dirt", false);
        setup(20, "tiles2/edge1", false);
    }

    public void setup(int index, String imagePath, boolean collision) {
        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/" + imagePath + ".png"));
            tile[index].image = new UtilityTool().scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filepath) {
        try {
            InputStream is = getClass().getResourceAsStream(filepath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int row = 0; row < gp.maxWorldRow; row++) {
                String[] tokens = br.readLine().split(" ");
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    mapTileNum[col][row] = Integer.parseInt(tokens[col]);
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        for (int row = 0; row < gp.maxWorldRow; row++) {
            for (int col = 0; col < gp.maxWorldCol; col++) {
                int tileNum = mapTileNum[col][row];
                int worldX = col * gp.tileSize;
                int worldY = row * gp.tileSize;
                int screenX = worldX - gp.player.worldX + gp.player.screenX;
                int screenY = worldY - gp.player.worldY + gp.player.screenY;

                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
        }
    }
}
