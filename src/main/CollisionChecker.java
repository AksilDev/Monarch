package main;

import entity.Entity;
public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }
    public void checkTile(Entity entity) {
        // === Get edges of the solid area ===
        int entityLeftWorldX   = entity.worldX + entity.solidArea.x;
        int entityRightWorldX  = entity.worldX + entity.solidArea.x + entity.solidArea.width - 1;
        int entityTopWorldY    = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height - 1;

        // === Predict next position based on direction ===
        switch (entity.direction) {
            case "up"    -> entityTopWorldY    -= entity.speed;
            case "down"  -> entityBottomWorldY += entity.speed;
            case "left"  -> entityLeftWorldX   -= entity.speed;
            case "right" -> entityRightWorldX  += entity.speed;
        }

        // === Convert to tile positions ===
        int leftCol   = entityLeftWorldX / gp.tileSize;
        int rightCol  = entityRightWorldX / gp.tileSize;
        int topRow    = entityTopWorldY / gp.tileSize;
        int bottomRow = entityBottomWorldY / gp.tileSize;

        // === Check all tiles the entity would touch ===
        for (int col = leftCol; col <= rightCol; col++) {
            for (int row = topRow; row <= bottomRow; row++) {
                // Bounds check (optional but safe)
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
