package entity;

import main.GamePanel;

public class EnemySpawner {

    private final GamePanel gp;
    private int enemyIndex = 0; // To manage enemy[] insertion

    public EnemySpawner(GamePanel gp) {
        this.gp = gp;
    }

    public void spawnWorld(int world) {
        enemyIndex = 0;
        for (int i = 0; i < gp.enemies.length; i++) {
            gp.enemies[i] = null;
        }

        switch (world) {
            case 1 -> spawnWorld1();
            case 2 -> spawnWorld2();
            case 3 -> spawnBossWorld();
        }
    }

    private void spawnWorld1() {
        addEnemy(new Goblin(gp, 19 * gp.tileSize, 10 * gp.tileSize));
        addEnemy(new Goblin(gp, 46 * gp.tileSize, 25 * gp.tileSize));
    }

    private void spawnWorld2() {
        addEnemy(new Goblin(gp, 15 * gp.tileSize, 15 * gp.tileSize));
        addEnemy(new Goblin(gp, 25 * gp.tileSize, 25 * gp.tileSize));
        addEnemy(new General(gp, 35 * gp.tileSize, 25 * gp.tileSize));
    }

    private void spawnBossWorld() {
        // addEnemy(new Boss(gp, ...));
    }

    private void addEnemy(Enemy e) {
        if (enemyIndex < gp.enemies.length) {
            gp.enemies[enemyIndex++] = e;
        }
    }
}
