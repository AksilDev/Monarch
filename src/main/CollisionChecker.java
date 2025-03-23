package main;
import entity.Entity;
public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp) {this.gp = gp;}

    public void checkTile(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        switch (entity.direction) {
            case "up" -> entityTopWorldY -= entity.speed;
            case "down" -> entityBottomWorldY += entity.speed;
            case "left" -> entityLeftWorldX -= entity.speed;
            case "right" -> entityRightWorldX += entity.speed;
        }

        // 3) Convert bounding box edges to tile coordinates
        int leftCol = entityLeftWorldX / gp.tileSize;
        int rightCol = entityRightWorldX / gp.tileSize;
        int topRow = entityTopWorldY / gp.tileSize;
        int bottomRow = entityBottomWorldY / gp.tileSize;

        for (int col = leftCol; col <= rightCol; col++) {
            for (int row = topRow; row <= bottomRow; row++) {
                if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) {
                    continue;
                }
                int tileNum = gp.tileM.mapTileNum[col][row];
                if (gp.tileM.tile[tileNum].collision) {
                    entity.collisionOn = true;
                    return;
                }
            }
        }
    }
}
