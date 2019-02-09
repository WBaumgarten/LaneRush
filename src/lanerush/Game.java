package lanerush;

import lanerush.structures.Castle;
import lanerush.structures.Mine;
import lanerush.structures.Woods;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Wernich
 */
public class Game extends Canvas implements Runnable {

    public static final int WIDTH = 1200, HEIGHT = 768;
    public final int MENU = 0, PLAY = 1, END = 2;
    private int state = MENU;
    private boolean isRunning = false;
    private Thread thread;
    private Handler handler;
    private BufferedImage spriteSheet = null;
    private SpriteSheet ss;
    private String infoText = "Press enter to start!";
    Font infoTextFont = new Font("04b03", Font.BOLD, 30);
    public static final Font defaultFont = new Font("FixedSys", Font.BOLD, 10);
    private JDialog loadingPopup;

    public Game() {
        createLoadingPopup();
        BufferedImageLoader loader = new BufferedImageLoader();
        spriteSheet = loader.loadImage("/spriteSheet.png");
        ss = new SpriteSheet(spriteSheet);
        removeLoadingPopup();
        new Window(WIDTH, HEIGHT, "Castle Rush", this);
        
        handler = new Handler();
        initGameObjects();
        this.addKeyListener(new KeyInput(handler, this, ss));
        this.addMouseListener(new MouseInput(handler));      
        start();       
    }

    private void createLoadingPopup() {
        JLabel loadingText = new JLabel("LOADING...", SwingConstants.CENTER);
        loadingText.setFont(infoTextFont);
        loadingText.setBorder(BorderFactory.createLineBorder(Color.black));
        loadingPopup = new JDialog();
        loadingPopup.setSize(260, 90);
        loadingPopup.setLocationRelativeTo(null);
        loadingPopup.setTitle("LaneRush");
        loadingPopup.setUndecorated(true);
        loadingPopup.add(loadingText);
        loadingPopup.setVisible(true);
    }

    private void removeLoadingPopup() {
        loadingPopup.dispose();
    }

    private void initGameObjects() {
        handler.addObject(new Castle(ID.Castle, true, handler, ss, this));
        handler.addObject(new Castle(ID.Castle, false, handler, ss, this));
        handler.addObject(new Mine(ID.Mine, true, handler, ss));
        handler.addObject(new Mine(ID.Mine, false, handler, ss));
        handler.addObject(new Woods(ID.Woods, true, handler, ss));
        handler.addObject(new Woods(ID.Woods, false, handler, ss));
    }

    public int getState() {
        return state;
    }

    public void startMatch() {
        infoText = "";
        if (state == END) {
            initGameObjects();
        }
        handler.addObject(new Cursor(ID.Cursor, true, handler, ss));
        handler.addObject(new Cursor(ID.Cursor, false, handler, ss));
        state = PLAY;
    }

    public void endMatch(boolean isPlayerLeft) {
        state = END;
        if (isPlayerLeft) {
            infoText = "Blue player won!";
        } else {
            infoText = "Red player won!";
        }
    }

    public void restartMatch() {
        infoText = "";
        handler.removeAllObjects();
        initGameObjects();
        handler.addObject(new Cursor(ID.Cursor, true, handler, ss));
        handler.addObject(new Cursor(ID.Cursor, false, handler, ss));
        state = PLAY;
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
        if (state != END) {
            handler.tick();
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        g.setFont(defaultFont);
        ///////// Draw Here ///////
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 1200, 800);

        g.setColor(Color.BLACK);
        for (int i = 224; i < 544; i += 32) {
            g.drawLine(0, i, 1200, i);
        }
//        g.setColor(Color.WHITE);
//        for (int i = 0; i < 224; i += 32) {
//            g.drawLine(0, i, 1200, i);
//        }
//        for (int i = 544; i < 1200; i += 32) {
//            g.drawLine(0, i, 1200, i);
//        }

        handler.render(g);
        g.setColor(Color.BLACK);
        g.setFont(infoTextFont);

        if (state == MENU) {
            g.drawString(infoText, (WIDTH / 2) - 220, 40);
        } else if (state == END) {
            g.drawString(infoText, (WIDTH / 2) - 200, 40);
            g.drawString("Press enter to play again.", (WIDTH / 2) - 250, 80);
        }
        ///////////////////////////
        g.dispose();
        bs.show();
    }

    public static void main(String args[]) {
        new Game();
    }

}
