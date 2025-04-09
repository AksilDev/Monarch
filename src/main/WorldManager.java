package main;

import entity.Boss;
import entity.General;
import entity.Goblin;
import object.OBJ_Key;
import object.OBJ_Portal;

public class WorldManager {

    GamePanel gp;

    public WorldManager(GamePanel gp) {
        this.gp = gp;
    }

    public void setupWorldContent() {

        System.out.println("🗺 Loading World: " + gp.currentWorld);

        gp.tileM.loadMap("/maps/world0" + gp.currentWorld + ".txt");
        gp.enemySpawner.spawnWorld(gp.currentWorld);

        gp.player.setDefaultValues();
        gp.hasKey = false;

        for (int i = 0; i < gp.enemies.length; i++) gp.enemies[i] = null;
        for (int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        if (gp.currentWorld == 1) {
            gp.obj[1] = new OBJ_Key(gp);
            gp.obj[1].worldX = 46 * gp.tileSize;
            gp.obj[1].worldY = 24 * gp.tileSize;

            gp.obj[2] = new OBJ_Portal(gp);
            gp.obj[2].worldX = 47 * gp.tileSize;
            gp.obj[2].worldY = 24 * gp.tileSize;

            gp.enemies[0] = new Goblin(gp, 19 * gp.tileSize, 10 * gp.tileSize);
            gp.enemies[1] = new Goblin(gp, 46 * gp.tileSize, 25 * gp.tileSize);

        } else if (gp.currentWorld == 2) {
            gp.obj[1] = new OBJ_Key(gp);
            gp.obj[1].worldX = 47 * gp.tileSize;
            gp.obj[1].worldY = 24 * gp.tileSize;

            gp.obj[2] = new OBJ_Portal(gp);
            gp.obj[2].worldX = 48 * gp.tileSize;
            gp.obj[2].worldY = 24 * gp.tileSize;

            gp.enemies[0] = new Goblin(gp, 11 * gp.tileSize, 22 * gp.tileSize);
            gp.enemies[1] = new Goblin(gp, 20 * gp.tileSize, 20 * gp.tileSize);
            gp.enemies[2] = new General(gp, 47 * gp.tileSize, 23 * gp.tileSize);

        } else if (gp.currentWorld == 3) {
            gp.obj[1] = new OBJ_Key(gp);
            gp.obj[1].worldX = 40 * gp.tileSize;
            gp.obj[1].worldY = 26 * gp.tileSize;

            gp.obj[2] = new OBJ_Portal(gp);
            gp.obj[2].worldX = 41 * gp.tileSize;
            gp.obj[2].worldY = 26 * gp.tileSize;

            gp.enemies[0] = new Goblin(gp, 15 * gp.tileSize, 10 * gp.tileSize);
            gp.enemies[1] = new General(gp, 25 * gp.tileSize, 20 * gp.tileSize);
            gp.enemies[2] = new General(gp, 30 * gp.tileSize, 15 * gp.tileSize);
            gp.enemies[3] = new Boss(gp, 35 * gp.tileSize, 26 * gp.tileSize);

        }
    }
    public void loadWorld(int world) {
        gp.currentWorld = world;
        setupWorldContent(); // or whatever method loads a world
    }

}
