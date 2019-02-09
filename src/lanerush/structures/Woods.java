package lanerush.structures;

import lanerush.Game;
import lanerush.Handler;
import lanerush.ID;
import lanerush.SpriteSheet;
import lanerush.Structure;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Wernich
 */
public class Woods extends Structure {
    
    public static final int HEIGHT = 128;
    public static final int WIDTH = 48;

    public Woods(ID id, boolean playerLeft, Handler handler, SpriteSheet ss) {
        super(id, playerLeft, handler, ss);
        this.setLane(2);
        health = 100;
        initCoords();
    }

    @Override
    public void initCoords() {
        if (playerLeft) {
            x = 20;
            y = Game.HEIGHT - HEIGHT - 20;
        } else {
            x = Game.WIDTH - WIDTH - 20;
            y = Game.HEIGHT - HEIGHT - 20;
        }
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(playerColor);
        g.fillRect(x, y, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        g.drawString(health + "", x, y);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.YELLOW);
        g2d.draw(getBounds());
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
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
    }

    @Override
    public void moveDown() {
    }

    @Override
    public void resourceInflux(int type) {
    }

}
