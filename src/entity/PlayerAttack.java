package entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PlayerAttack {
    private final Player player;

    public PlayerAttack(Player player) {
        this.player = player;
    }

    public boolean updateAttack() {
        if (player.attacking) {
            handle(player.attackFrames, 1);
            return true;
        }
        if (player.usingSpecial) {
            handle(player.specialFrames, 2);
            return true;
        }
        if (player.usingUltimate) {
            handle(player.ultFrames, 4);
            return true;
        }
        return false;
    }

    private void handle(BufferedImage[][] frames, int damage) {
        player.attackTimer++;

        if (player.attackFrameIndex == 2 && !player.alreadyHit) {
            checkHitEnemies(damage);
            player.alreadyHit = true;
        }

        if (player.attackTimer > 2) {
            player.attackFrameIndex++;
            player.attackTimer = 0;

            if (player.attackFrameIndex >= player.ATTACK_FRAMES_PER_DIRECTION) {
                player.attackFrameIndex = 0;
                player.attacking = false;
                player.usingSpecial = false;
                player.usingUltimate = false;
                player.alreadyHit = false;
            }
        }
    }

    private void checkHitEnemies(int damage) {
        int slashX = player.worldX;
        int slashY = player.worldY;
        int slashW = 40;
        int slashH = 40;

        switch (player.direction) {
            case "down" -> slashY += 40;
            case "up" -> slashY -= 40;
            case "left" -> slashX -= 40;
            case "right" -> slashX += 40;
        }

        Rectangle slashArea = new Rectangle(slashX, slashY, slashW, slashH);

        for (var enemy : player.gp.enemies) {
            if (enemy != null && enemy.alive) {
                Rectangle enemyBox = new Rectangle(
                        enemy.worldX + enemy.solidArea.x,
                        enemy.worldY + enemy.solidArea.y,
                        enemy.solidArea.width,
                        enemy.solidArea.height
                );

                if (slashArea.intersects(enemyBox)) {
                    enemy.takeDamage(damage);
                }
            }
        }
    }
}