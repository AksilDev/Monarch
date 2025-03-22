package main;

import entity.Player;
import object.SuperObject;
import object.OBJ_Heart;
import object.OBJ_Key;
import object.OBJ_Portal;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    public final int originalTileSize = 16; // 16x16 tile
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile

    public final int maxScreenCol = 33;
    public final int maxScreenRow = 20;


    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    //game state
    public static final int PLAY_STATE = 0;
    public static final int DEATH_STATE = 1;
    //public static final int PAUSE_STATE = 1;
    public int gameState = PLAY_STATE;

    // FPS
    int FPS = 60;

    // GAME ENTITIES
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[20]; // up to 20 objects

    // UI
    public UI ui = new UI(this);

    Thread gameThread;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        setupGame();
    }

    public void setupGame() {


       //manipulate key and portal location
        obj[1] = new OBJ_Key(this);
        obj[1].worldX = 46 * tileSize;
        obj[1].worldY = 24 * tileSize;

        // Place a portal at tile (15,15)
        obj[2] = new OBJ_Portal(this);
        obj[2].worldX = 48 * tileSize;
        obj[2].worldY = 24 * tileSize;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta > 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (gameState == PLAY_STATE) {
            player.update();
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    obj[i].update();
                }
            }

            pickUpObject();
        }
    }
    public void pickUpObject() {
        for (int i = 0; i < obj.length; i++) {
            SuperObject object = obj[i];
            if (object != null) {
                Rectangle objArea = new Rectangle(
                        object.worldX,
                        object.worldY,
                        tileSize,
                        tileSize
                );

                Rectangle playerArea = new Rectangle(
                        player.worldX + player.solidArea.x,
                        player.worldY + player.solidArea.y,
                        player.solidArea.width,
                        player.solidArea.height
                );

                if (playerArea.intersects(objArea)) {
                    if (object.name.equals("Heart")) {
                        obj[i] = null;
                    } else if (object.name.equals("Key")) {
                        obj[i] = null;
                    } else if (object.name.equals("Portal")) {
                    }
                }
            }
        }
    }

    private void drawObjects(Graphics2D g2) {
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                int screenX = obj[i].worldX - player.worldX + player.screenX;
                int screenY = obj[i].worldY - player.worldY + player.screenY;
                obj[i].draw(g2, screenX, screenY, tileSize);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == PLAY_STATE) {
            tileM.draw(g2);
            drawObjects(g2); // calls the method above
            player.draw(g2);
            ui.draw(g2);
        }
        else if (gameState == DEATH_STATE) {
            tileM.draw(g2);
            drawObjects(g2);
            player.draw(g2);
            ui.drawDeathScreen(g2);
        }

        g2.dispose();
    }
}