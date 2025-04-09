package main;

import object.*;
import main.GamePanel;

public class ObjectManager {

    GamePanel gp;

    public ObjectManager(GamePanel gp) {
        this.gp = gp;
    }

    public void placeObjectsForWorld(int world) {
        for (int i = 0; i < gp.obj.length; i++) {
            gp.obj[i] = null;
        }

        switch (world) {
            case 1 -> {
                gp.obj[0] = new OBJ_Key(gp);
                gp.obj[0].worldX = 46 * gp.tileSize;
                gp.obj[0].worldY = 24 * gp.tileSize;

                gp.obj[1] = new OBJ_Portal(gp);
                gp.obj[1].worldX = 47 * gp.tileSize;
                gp.obj[1].worldY = 24 * gp.tileSize;
            }
            case 2 -> {
                gp.obj[0] = new OBJ_Key(gp);
                gp.obj[0].worldX = 47 * gp.tileSize;
                gp.obj[0].worldY = 24 * gp.tileSize;

                gp.obj[1] = new OBJ_Portal(gp);
                gp.obj[1].worldX = 49 * gp.tileSize;
                gp.obj[1].worldY = 24 * gp.tileSize;
            }
            case 3 -> {
                gp.obj[0] = new OBJ_Key(gp);
                gp.obj[0].worldX = 40 * gp.tileSize;
                gp.obj[0].worldY = 26 * gp.tileSize;

                gp.obj[1] = new OBJ_Portal(gp);
                gp.obj[1].worldX = 41 * gp.tileSize;
                gp.obj[1].worldY = 26 * gp.tileSize;
            }
        }
    }
}
