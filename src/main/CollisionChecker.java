package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {
        // 1) Get the entity’s bounding box in world coordinates
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // 2) Depending on direction, adjust the top/bottom/left/right by 'entity.speed'
        //    so we check where the entity will be after moving.
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

        // 4) Loop through all columns and rows the bounding box might overlap
        for (int col = leftCol; col <= rightCol; col++) {
            for (int row = topRow; row <= bottomRow; row++) {

                // Make sure we're not out of map bounds
                if (col < 0 || col >= gp.maxWorldCol || row < 0 || row >= gp.maxWorldRow) {
                    // If out of bounds, treat it as collision or just ignore
                    continue;
                }

                int tileNum = gp.tileM.mapTileNum[col][row];
                if (gp.tileM.tile[tileNum].collision) {
                    entity.collisionOn = true;
                    return; // We found a colliding tile, no need to check further
                }
            }
        }
    }
}
