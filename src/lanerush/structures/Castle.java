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
public class Castle extends Structure {
    
    public static final int WIDTH = 64;
    public static final int HEIGHT = 96;
    public static final int WALLGAP = 100;
    private Game game;

    public Castle(ID id, boolean playerLeft, Handler handler, SpriteSheet ss, Game game) {
        super(id, playerLeft, handler, ss);
        this.game = game;
        this.setLane(0);
        health = 1000;
        initCoords();
    }

    @Override
    public void tick() {
        if (health <= 0) {
            game.endMatch(isPlayerLeft());
        }
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
        if (playerLeft) {
            return new Rectangle (x + WIDTH + WALLGAP, y - HEIGHT, 16, 288);
        } else {
            return new Rectangle (x - WALLGAP - 16, y - HEIGHT , 16, 288);
        }       
    }

    @Override
    public void initCoords() {
        if (playerLeft) {
            x = 20;
            y = 320;
        } else {
            x = Game.WIDTH - WIDTH - 20;
            y = 320;
        }
    }

    @Override
    public void loseHealth(int damage) {
        health -= damage;
    }

    @Override
    public int getHealth() {
        return health;
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
