package main;

import entity.Player;
import entity.Enemy;
import entity.Goblin;
import object.*;
import tile.TileManager;
import entity.General;
import entity.EnemySpawner;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // ========== SCREEN SETTINGS ==========
    public final int originalTileSize = 16;
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 33;
    public final int maxScreenRow = 20;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // ========== WORLD SETTINGS ==========
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public int currentWorld = 2;
    public boolean DEBUG_MODE = true;


    // ========== GAME STATES ==========
    public static final int PLAY_STATE = 0;
    public static final int DEATH_STATE = 1;
    public static final int WIN_STATE = 2;
    public int gameState = PLAY_STATE;

    // ========== GAME ENTITIES ==========
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);

    public Player player = new Player(this, keyH);
    public Enemy[] enemies = new Enemy[10];
    public SuperObject[] obj = new SuperObject[20];
    public EnemySpawner enemySpawner = new EnemySpawner(this);

    // ========== UI & SYSTEM ==========
    public UI ui = new UI(this);
    public boolean hasKey = false;
    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        setupWorldContent();
    }

    public void setupWorldContent() {
        System.out.println("🗺 Loading World: " + currentWorld);
        tileM.loadMap("/maps/world0" + currentWorld + ".txt");
        enemySpawner.spawnWorld(currentWorld);

        // Reset player and flags
        player.setDefaultValues();
        hasKey = false;

        // Clear previous state
        for (int i = 0; i < enemies.length; i++) enemies[i] = null;
        for (int i = 0; i < obj.length; i++) obj[i] = null;

        // Set up world 1 only for now
        if (currentWorld == 1) {
            obj[1] = new OBJ_Key(this);
            obj[1].worldX = 46 * tileSize;
            obj[1].worldY = 24 * tileSize;

            obj[2] = new OBJ_Portal(this);
            obj[2].worldX = 47 * tileSize;
            obj[2].worldY = 24 * tileSize;

            enemies[0] = new Goblin(this, 19 * tileSize, 10 * tileSize);
            enemies[1] = new Goblin(this, 46 * tileSize, 25 * tileSize);
        }else if (currentWorld == 2) {
            tileM.loadMap("/maps/world02.txt"); // Make sure path is correct

            obj[1] = new OBJ_Key(this);
            obj[1].worldX = 46 * tileSize;
            obj[1].worldY = 24 * tileSize;

            obj[2] = new OBJ_Portal(this);
            obj[2].worldX = 47 * tileSize;
            obj[2].worldY = 24 * tileSize;

            enemies[0] = new Goblin(this, 11 * tileSize, 22 * tileSize);
            enemies[1] = new Goblin(this, 20 * tileSize, 20 * tileSize);
             enemies[2] = new General(this, 45 * tileSize, 23 * tileSize);
        }

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / 60;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (gameState == PLAY_STATE) {
            player.update();

            for (SuperObject o : obj) {
                if (o != null) o.update();
            }

            for (int i = 0; i < enemies.length; i++) {
                Enemy e = enemies[i];
                if (e != null) {
                    e.update();
                    if (!e.alive) {
                        enemies[i] = null;
                    }
                }
            }

            pickUpObject();
        }
    }

    public void pickUpObject() {
        Rectangle playerArea = new Rectangle(
                player.worldX + player.solidArea.x,
                player.worldY + player.solidArea.y,
                player.solidArea.width,
                player.solidArea.height
        );

        for (int i = 0; i < obj.length; i++) {
            SuperObject object = obj[i];
            if (object == null) continue;

            Rectangle objArea = new Rectangle(object.worldX, object.worldY, tileSize, tileSize);

            if (playerArea.intersects(objArea)) {
                switch (object.name) {
                    case "Key" -> {
                        hasKey = true;
                        obj[i] = null;
                    }
                    case "Portal" -> {
                        if (hasKey && areAllEnemiesDead()) {
                            currentWorld++;
                            if (currentWorld > 3) {
                                gameState = WIN_STATE;
                            } else {
                                setupWorldContent();
                            }
                        }
                    }
                    case "Heart" -> obj[i] = null;
                }
            }
        }
    }

    public boolean areAllEnemiesDead() {
        for (Enemy e : enemies) {
            if (e != null && e.alive) return false;
        }
        return true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);
        drawObjects(g2);
        drawEnemies(g2);
        player.draw(g2);

        if (gameState == DEATH_STATE) {
            ui.drawDeathScreen(g2);
        } else {
            ui.draw(g2);
        }

        if (gameState == WIN_STATE) {
            g2.setFont(new Font("Arial", Font.BOLD, 60));
            g2.setColor(Color.YELLOW);
            String msg = currentWorld > 3 ? "You Win!" : "Floor 1 Cleared!";
            int msgWidth = g2.getFontMetrics().stringWidth(msg);
            g2.drawString(msg, screenWidth / 2 - msgWidth / 2, screenHeight / 2);
        }

        g2.dispose();
    }

    private void drawObjects(Graphics2D g2) {
        for (SuperObject o : obj) {
            if (o != null) {
                int screenX = o.worldX - player.worldX + player.screenX;
                int screenY = o.worldY - player.worldY + player.screenY;
                o.draw(g2, screenX, screenY, tileSize);
            }
        }
    }

    private void drawEnemies(Graphics2D g2) {
        for (Enemy e : enemies) {
            if (e != null) e.draw(g2);
        }
    }
}
