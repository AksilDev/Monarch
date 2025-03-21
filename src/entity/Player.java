package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public Player (GamePanel gp, KeyHandler keyH){

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues(){

        worldX = gp.tileSize * 10;
        worldY = gp.tileSize * 10;
        speed = 4;
        direction ="down";
    }
    public void getPlayerImage(){

        up1 = setup("up1");
        up2 = setup("up2");
        up3 = setup("up3");
        up4 = setup("up4");
        down1 = setup("down1");
        down2 = setup("down2");
        down3 = setup("down3");
        down4 = setup("down4");
        left1 = setup("left1");
        left2 = setup("left2");
        left3 = setup("left3");
        left4 = setup("left4");
        right1 = setup("right1");
        right2 = setup("right2");
        right3 = setup("right3");
        right4 = setup("right4");
    }

    public BufferedImage setup(String imageName){
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/player/"+ imageName +".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        }catch (IOException e){
            e.printStackTrace();
        }
        return image;
    }

    public void update(){

        if(keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true){

            if(keyH.upPressed == true){
                direction ="up";
            }else if(keyH.downPressed == true){
                direction ="down";
            }else if(keyH.leftPressed == true){
                direction ="left";
            }else if(keyH.rightPressed == true){
                direction ="right";
            }
            collisionOn = false;
            gp.cChecker.checkTile(this);

            if(collisionOn == false){
                switch(direction){
                    case "up":
                        worldY -= speed;break;
                    case "down":
                        worldY += speed;break;
                    case "left":
                        worldX -= speed;break;
                    case "right":
                        worldX += speed;break;
                }
            }
            spriteCounter++;
            if(spriteCounter > 12){
                if(spriteNum == 1){
                    spriteNum = 2;
                }else if(spriteNum == 2){
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

        }


    }
    public void draw(Graphics2D g2){


        BufferedImage image = null;

        switch(direction){
            case "up":
            if(spriteNum == 1){
                image = up1;
            }
            if(spriteNum == 2){
                image = up2;
            }
            if(spriteNum == 3){
                image = up3;
            }
            if(spriteNum == 4){
                image = up4;
            }
            break;



            case "down":
            if(spriteNum == 1){
                image = down1;
            }
            if(spriteNum == 2){
                image = down2;
            }

            if(spriteNum == 4){
                image = down4;
            }
            break;


            case "left":
            if(spriteNum == 1){
                image = left1;
            }
            if(spriteNum == 2){
                image = left2;
            }
            if(spriteNum == 3){
                image = left3;
            }
            if(spriteNum == 4){
                image = left4;
            }
            break;



            case "right":
            if(spriteNum == 1){
                image = right1;
            }
            if(spriteNum == 2){
                image = right2;
            }
            if(spriteNum == 3){
                image = right3;
            }
            if(spriteNum == 4){
                image = right4;
            }
            break;
        }
            g2.drawImage(image, screenX,screenY, gp.tileSize, gp.tileSize, null);
    }
}
