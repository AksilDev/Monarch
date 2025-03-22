package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends SuperObject{

    GamePanel gp;

    public OBJ_Heart(GamePanel gp){
        this.gp = gp;
        name ="heart";

        try{
            image = ImageIO.read(getClass().getResourceAsStream("/object/full.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/object/half.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/object/blank.png"));
            UTool.scaleImage(image, gp.tileSize,gp.tileSize);
            UTool.scaleImage(image2, gp.tileSize,gp.tileSize);
            UTool.scaleImage(image3, gp.tileSize,gp.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

