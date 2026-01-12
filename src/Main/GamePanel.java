package Main;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyAdapter;


public class GamePanel extends JPanel implements Runnable {

    public static final int width = 1280;
    public static final int height = 720;
    final int FPS = 60;
    Thread gameThread;
    PlayManager pm;


    public GamePanel() {

        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.WHITE);
        this.setLayout(null);

        this.addKeyListener(new KeyHandler());
        this.setFocusable(true);

        pm = new PlayManager();
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        // game loop
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }

    }

    private void update() {
        if( KeyHandler.pausePressed == false){
            pm.update();
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        pm.draw(g2d);
    }
}