package main;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class KeyHandler implements KeyListener {

    GamePanel gp;


    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }
    public boolean upPressed, downPressed, leftPressed, rightPressed;



    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){upPressed = true;}
        if(code == KeyEvent.VK_S){downPressed = true;}
        if(code == KeyEvent.VK_A){leftPressed = true; }
        if(code == KeyEvent.VK_D){rightPressed = true;}
        //<------
        if(code == KeyEvent.VK_M){
            gp.player.currentHP--;
                if(gp.player.currentHP < 0){
                    gp.player.currentHP = 0;
                }
        }
        //<------
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

    if(gp.gameState == GamePanel.DEATH_STATE) {
        if(code == KeyEvent.VK_ENTER){
            gp.player.currentHP = gp.player.maxHP;
            gp.player.worldX = gp.tileSize * 2;
            gp.player.worldY = gp.tileSize * 27;
            gp.gameState = GamePanel.PLAY_STATE;
        }
    }else{
        if (code == KeyEvent.VK_W) {upPressed = false;}
        if (code == KeyEvent.VK_S) {downPressed = false;}
        if (code == KeyEvent.VK_A) {leftPressed = false;}
        if (code == KeyEvent.VK_D) {rightPressed = false;}
    }

    }
}
