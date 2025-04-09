package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public final GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;

            case KeyEvent.VK_J -> gp.player.attackHandler.startAttack();
            case KeyEvent.VK_K -> {
                if (gp.player.attackHandler.isSpecialReady())
                    gp.player.attackHandler.startSpecial();
            }
            case KeyEvent.VK_L -> {
                if (gp.player.attackHandler.isUltimateReady())
                    gp.player.attackHandler.startUltimate();
            }
        }if(code == KeyEvent.VK_K && !gp.player.usingSpecial && !gp.player.attacking) {
            gp.player.startSpecial();
        }

        if(code == KeyEvent.VK_L && !gp.player.usingUltimate && !gp.player.attacking) {
            gp.player.startUltimate();
        }



    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
