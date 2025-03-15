package main;

import entity.Player;

import java.awt.*;
import javax.swing.JPanel;
public class GamePanel extends JPanel implements Runnable{
// SCREEN SETTINGS

final int originalTileSize= 16; // 16x16 tile
final int scale = 3;


public final int tileSize = originalTileSize * scale; // 48x48 tile
final int maxScreenCol = 33;
final int maxScreenRow = 20;
//    final int maxScreenCol = 16;
//    final int maxScreenRow = 12;
final int screenWidth = tileSize * maxScreenCol; // 1584 pixels
final int screenHeight = tileSize * maxScreenRow; // 960 pixels

    int FPS = 60;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new  Player(this,keyH);

    //static position of char
    int playerX = 50;
    int playerY = 50;
    int playerSpeed = 4;

        public GamePanel() {
            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            this.setBackground(Color.black);
            this.setDoubleBuffered(true);
            this.addKeyListener(keyH);
            this.setFocusable(true);
        }
    public void startGameThread(){
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
            if(delta>1){
                update();
                repaint();
                delta--;
            }

        }
    }
        public void update(){

            player.update();
        }


        public void paintComponent(Graphics g){

            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;

            player.draw(g2);

            g2.dispose();
        }
    }

