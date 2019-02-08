package lanerush;

import lanerush.structures.Castle;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import lanerush.structures.Mine;

/**
 *
 * @author Wernich
 */
public class Cursor extends GameObject {

    private final int WIDTH = 32, HEIGHT = 32;

    private final int GOLDPASSIVE = 5, TIMBERPASSIVE = 5;
    private final int GOLDINFLUX = 40, TIMBERINFLUX = 40;
    public static final int GOLD = 0, TIMBER = 1;
    private final long resourceTime = 1000000000;
    private long resourceTimer;
    Font resourceFont = new Font("FixedSys", Font.BOLD, 15);

    public Cursor(ID id, boolean playerLeft, Handler handler, SpriteSheet ss) {
        super(id, playerLeft, handler, ss);
        initCoords();
        lane = 5;
        resourceTimer = System.nanoTime();
    }

    @Override
    public void tick() {
        long currTime = System.nanoTime();
        if (currTime - resourceTimer > resourceTime) {
            gold += GOLDPASSIVE;
            timber += TIMBERPASSIVE;
            resourceTimer = System.nanoTime();
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawRect(x, y, 32, 32);
        g.setFont(resourceFont);
        if (isPlayerLeft()) {
            g.setColor(Color.YELLOW);
            g.drawString("GOLD: " + gold, Mine.WIDTH + 60, 20);
            g.setColor(new Color(139, 69, 19));
            g.drawString("TIMBER: " + timber, Mine.WIDTH + 60, 40);
        } else {
            g.setColor(Color.YELLOW);
            g.drawString("GOLD: " + gold, Game.WIDTH - Mine.WIDTH - 120, 20);
            g.setColor(new Color(139, 69, 19));
            g.drawString("TIMBER: " + timber, Game.WIDTH - Mine.WIDTH - 120, 40);
        }
        g.setFont(Game.defaultFont);
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    @Override
    public void loseHealth(int damage) {
    }

    @Override
    public int getHealth() {
        return 0;
    }

    @Override
    public void moveUp() {
        if (lane != 9) {
            lane++;
            y -= 32;
        }
    }

    @Override
    public void moveDown() {
        if (lane != 1) {
            lane--;
            y += 32;
        }
    }

    private void initCoords() {
        if (isPlayerLeft()) {
            x = 20 + (int) (Castle.WIDTH * 1.5);
            y = 352;
        } else {
            x = Game.WIDTH - 20 - (int) (Castle.WIDTH * 1.5) - WIDTH;
            y = 352;
        }

    }

    @Override
    public void resourceInflux(int type) {
        if (type == GOLD) {
            gold += GOLDINFLUX;
        } else if (type == TIMBER) {
            timber += TIMBERINFLUX;
        }
    }

}
