package entity;
import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    GamePanel gp;
    KeyHandler keyH;

    public int maxHP = 10;
    public int currentHP = 10;

    //walking frame
    public BufferedImage[][] frames;

    //attack frame
    public boolean attacking = false;
    public BufferedImage[][] attackframes;
    private int attackFrameIndex = 0;
    private int attackTimer = 0;
    private final  int ATTACK_FRAMES_PER_DIRECTION = 8;


    //index and direction
    private int frameIndex;
    private int directionNum;

    //world building
    public final int screenX;
    public final int screenY;
    public int spriteCounter = 0;

    public Player (GamePanel gp, KeyHandler keyH){

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 60;
        solidArea.height = 60;

        setDefaultValues();
        loadSheet();
        loadAttackSheet();
    }
    public void setDefaultValues(){

        worldX = gp.tileSize * 2;
        worldY = gp.tileSize * 27;
        speed = 4;
        direction ="down";
    }


    public void loadSheet() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResourceAsStream("/player/orc2_full.png")
            );
            UtilityTool uTool = new UtilityTool();

            int rows = 4, cols = 8;
            int frameWidth = 64, frameHeight = 64;

            //walk frames
            frames = new BufferedImage[rows][cols];

            for(int row = 0; row < rows; row++){
                for(int col = 0; col < cols; col++){
                    BufferedImage sub = sheet.getSubimage(
                            col * frameWidth,
                            row * frameHeight,
                            frameWidth,
                            frameHeight
                    );
                    // scale each frame to tileSize
                    frames[row][col] = uTool.scaleImage(sub, gp.tileSize * 2, gp.tileSize * 2 );
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAttackSheet() {
        try {
            BufferedImage sheet = ImageIO.read(
                    getClass().getResourceAsStream("/player/orc2_attack.png")
            );
            UtilityTool uTool = new UtilityTool();

            int rows = 4, cols = 8;
            int frameWidth = 64, frameHeight = 64;


            attackframes = new BufferedImage[rows][cols];

            for(int row = 0; row < rows; row++){
                for(int col = 0; col < cols; col++){
                    BufferedImage sub = sheet.getSubimage(
                            col * frameWidth,
                            row * frameHeight,
                            frameWidth,
                            frameHeight
                    );
                    // scale each frame to tileSize
                    sub = uTool.scaleImage(sub, gp.tileSize * 2, gp.tileSize * 2 );
                    attackframes[row][col] = sub;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void takeDamage(int damage) {
        currentHP -= damage;
        if(currentHP < 0) {
            currentHP = 0;
        }

        if(currentHP == 0){
            gp.gameState = GamePanel.DEATH_STATE;
        }

    }


    public void update() {

        if(attacking){
            attackTimer++;
            if(attackTimer > 2){
                attackFrameIndex++;
                attackTimer = 0;
                if(attackFrameIndex >= ATTACK_FRAMES_PER_DIRECTION){
                    attackFrameIndex = 0;
                    attacking = false;
                }
            }
            return;
        }


        boolean moving = false;
        if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){moving = true;}
        if(moving){
            if(keyH.upPressed){
                direction = "up";
                directionNum = 1;
            }
            else if(keyH.downPressed){
                direction = "down";
                directionNum = 0;
            }
            else if(keyH.leftPressed){
                direction = "left";
                directionNum = 2;
            }
            else if(keyH.rightPressed){
                direction = "right";
                directionNum = 3;
            }

            collisionOn = false;
            gp.cChecker.checkTile(this);
            if(!collisionOn){
                switch(direction){
                    case "up"   -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right"-> worldX += speed;
                }
            }


            spriteCounter++;
            if(spriteCounter > 12){
                frameIndex++;
                if(frameIndex >= frames[directionNum].length){
                    frameIndex = 0;
                }
                spriteCounter = 0;
            }
        }
        else {
            frameIndex = 0;
        }

        if(currentHP <= 0){
            currentHP = 0;
            gp.gameState = GamePanel.DEATH_STATE;
        }


    }

    public void startAttack(){
        attacking = true;
        attackFrameIndex = 0;
        attackTimer = 0;
    }


    public void draw(Graphics2D g2){
        BufferedImage image;

        if(attacking){

            image = attackframes[directionNum][attackFrameIndex];
        }else{
            image = frames[directionNum][frameIndex];
        }
            g2.drawImage(image,screenX,screenY, null);
    }


}
