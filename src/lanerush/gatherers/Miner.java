package lanerush.gatherers;

import lanerush.Cursor;
import lanerush.GameObject;
import lanerush.Gatherer;
import lanerush.Handler;
import lanerush.ID;
import lanerush.SpriteSheet;
import lanerush.structures.Castle;
import lanerush.structures.Mine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author Wernich
 */
public class Miner extends Gatherer {

    public static final int GOLDCOST = 30, TIMBERCOST = 30;
    public static final int WIDTH = 32;
    public static final int HEIGHT = 32;
    private int randSpot;

    public Miner(ID id, boolean playerLeft, int lane, Handler handler, SpriteSheet ss) {
        super(id, playerLeft, handler, ss);
        this.setLane(lane);
        state = ATBASE;
        speed = 1;
        gatherTime = 2000000000;
        initCoords();
    }

    @Override
    public void initCoords() {
        for (GameObject object : handler.object) {
            if (object.getId() == ID.Mine && object.isPlayerLeft() == isPlayerLeft()) {
                destX = object.getX();
                destY = object.getY();
            } else if (object.getId() == ID.Castle && object.isPlayerLeft() == isPlayerLeft()) {
                castleX = object.getX() + (Castle.WIDTH / 2) - (WIDTH / 2);
                castleY = object.getY() + (Castle.HEIGHT / 2) - (HEIGHT / 2);
            }
        }
        x = castleX;
        y = castleY;
    }

    @Override
    public void tick() {
        if (state == ATBASE) {
            Random rand = new Random();
            randSpot = rand.nextInt(((destX + Mine.WIDTH - WIDTH) - destX) + 1)
                    + destX;
            state = TOGATHER;
        } else if (state == TOGATHER) {
            if (randSpot > x && y < castleY - (HEIGHT * 1.5)) {
                x += speed;
            } else if (randSpot < x && y < castleY - (HEIGHT * 1.5)) {
                x -= speed;
            }
            if (destY + 32 >= y) {
                gatherTimer = System.nanoTime();
                state = GATHERING;
            } else if (destY < y) {
                y -= speed;
            }
        } else if (state == GATHERING) {
            long currTime = System.nanoTime();
            if (currTime - gatherTimer > gatherTime) {
                state = TOBASE;
            }
        } else if (state == TOBASE) {
            if (castleX > x) {
                x += speed;
            } else if (castleX < x) {
                x -= speed;
            }
            if (castleY <= y) {
                state = ATBASE;
                for (GameObject object : handler.object) {
                    if (object.getId() == ID.Cursor && object.isPlayerLeft() == isPlayerLeft()) {
                        object.resourceInflux(Cursor.GOLD);
                        return;
                    }
                }
            } else if (castleY > y) {
                y += speed;
            }
        }

    }

    @Override
    public void render(Graphics g) {
        g.setColor(playerColor);
        g.fillRect(x, y, 32, 32);
        g.setColor(Color.YELLOW);
        g.fillRect(x + 12, y + 12, 8, 8);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
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
