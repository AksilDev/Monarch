package entity;

import main.GamePanel;
import main.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public Player (GamePanel gp, KeyHandler keyH){

        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues(){

        x = 50;
        y = 50;
        speed = 4;
        direction ="down";
    }
    public void getPlayerImage(){

        try{
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/up1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/up2.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/player/up3.png"));
            up4 = ImageIO.read(getClass().getResourceAsStream("/player/up4.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/player/down1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/down2.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/player/down3.png"));
            down4 = ImageIO.read(getClass().getResourceAsStream("/player/down4.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/player/left1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/left2.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player/left3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/player/left4.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/player/right1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/right2.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player/right3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/player/right4.png"));
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void update(){

        if(keyH.upPressed == true){
            direction ="up";
            y -= speed;
        }else if(keyH.downPressed == true){
            direction ="down";
            y += speed;
        }else if(keyH.leftPressed == true){
            direction ="left";
            x -= speed;
        }else if(keyH.rightPressed == true){
            direction ="right";
            x += speed;
        }
    }
    public void draw(Graphics2D g2){
//        g2.setColor(Color.white);
//        g2.fillRect(x,y,gp.tileSize,gp.tileSize);

        BufferedImage image = null;

        switch(direction){
            case "up": image = up1; break;
            case "down": image = down1; break;
            case "left": image = left1; break;
            case "right": image = right1; break;
        }
            g2.drawImage(image, x,y, gp.tileSize, gp.tileSize, null);
    }
}
