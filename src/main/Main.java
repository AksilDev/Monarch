package main;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        // === Create game window ===
        JFrame window = new JFrame("Monarch’s Descent");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        // === Add game panel to window ===
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); // Fit to preferred size
        window.setLocationRelativeTo(null); // Center on screen
        window.setVisible(true);

        // === Start game thread ===
        gamePanel.startGameThread();
    }
}
