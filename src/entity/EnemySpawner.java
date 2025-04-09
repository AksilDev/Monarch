package entity;

import main.GamePanel;

public class EnemySpawner {

    private final GamePanel gp;

    public EnemySpawner(GamePanel gp) {
        this.gp = gp;
    }

    public void spawnWorld(int world) {
        for (int i = 0; i < gp.enemies.length; i++) gp.enemies[i] = null;

        switch (world) {
            case 1 -> spawnWorld1();
            case 2 -> spawnWorld2();
            case 3 -> spawnWorld3();
        }
    }

    private void spawnWorld1() {
        gp.enemies[0] = new Goblin(gp, 19 * gp.tileSize, 10 * gp.tileSize);
        gp.enemies[1] = new Goblin(gp, 46 * gp.tileSize, 25 * gp.tileSize);
    }

    private void spawnWorld2() {
        gp.enemies[0] = new Goblin(gp, 15 * gp.tileSize, 15 * gp.tileSize);
        gp.enemies[1] = new Goblin(gp, 25 * gp.tileSize, 25 * gp.tileSize);
        gp.enemies[2] = new General(gp, 35 * gp.tileSize, 25 * gp.tileSize);
    }

    private void spawnWorld3() {
        gp.enemies[0] = new Goblin(gp, 15 * gp.tileSize, 15 * gp.tileSize);
        gp.enemies[1] = new Goblin(gp, 25 * gp.tileSize, 25 * gp.tileSize);
        gp.enemies[2] = new General(gp, 30 * gp.tileSize, 15 * gp.tileSize);
        gp.enemies[3] = new Boss(gp, 35 * gp.tileSize, 26 * gp.tileSize);
    }
}