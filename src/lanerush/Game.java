package lanerush;

import lanerush.gatherers.Miner;
import lanerush.structures.Castle;
import lanerush.structures.Mine;
import lanerush.structures.Woods;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wernich
 */
public class Game extends Canvas implements Runnable {

    public static final int WIDTH = 1200, HEIGHT = 768;
    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private BufferedImage spriteSheet = null;
    private SpriteSheet ss;

    public Game() {
        new Window(WIDTH, HEIGHT, "Castle Rush", this);
        start();

        handler = new Handler();
        
        
        BufferedImageLoader loader = new BufferedImageLoader();
        spriteSheet = loader.loadImage("/spriteSheet.png");
        ss = new SpriteSheet(spriteSheet);
        
        this.addKeyListener(new KeyInput(handler, this, ss));
        this.addMouseListener(new MouseInput(handler));

        handler.addObject(new Castle(ID.Castle, true, handler, ss));
        handler.addObject(new Castle(ID.Castle, false, handler, ss));
        handler.addObject(new Cursor(ID.Cursor, true, handler, ss));
        handler.addObject(new Cursor(ID.Cursor, false, handler, ss));
        handler.addObject(new Mine(ID.Mine, true, handler, ss));
        handler.addObject(new Mine(ID.Mine, false, handler, ss));
        handler.addObject(new Woods(ID.Woods, true, handler, ss));
        handler.addObject(new Woods(ID.Woods, false, handler, ss));
    }

    private void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }
        stop();
    }

    public void tick() {
        handler.tick();
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        ///////// Draw Here ///////
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 1200, 800);

        g.setColor(Color.BLACK);
//        for (int i = 0; i < 496; i += 32) {
//            g.drawLine(i, 206, i, 800);
//        }
        for (int i = 224; i < 544; i += 32) {
            g.drawLine(0, i, 1200, i);
        }
        g.setColor(Color.WHITE);
        for (int i = 0; i < 224; i += 32) {
            g.drawLine(0, i, 1200, i);
        }
        for (int i = 544; i < 1200; i += 32) {
            g.drawLine(0, i, 1200, i);
        }
        
        

        handler.render(g);
        ///////////////////////////
        g.dispose();
        bs.show();
    }

    public static void main(String args[]) {
        new Game();
    }

}
