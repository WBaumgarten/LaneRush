package lanerush;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Wernich
 */
public abstract class GameObject {

    protected int x, y;    
    protected ID id;
    protected boolean playerLeft;
    protected Color playerColor;
    protected Handler handler;
    protected SpriteSheet ss;
    protected int gold = 0;
    protected int timber = 0;
    protected int lane = 0;
    
    public GameObject(ID id, boolean playerLeft, Handler handler, SpriteSheet ss) {
        this.id = id;
        this.playerLeft = playerLeft;
        this.handler = handler;
        this.ss = ss;
        if (playerLeft) {
            playerColor = Color.RED;
        } else {
            playerColor = Color.BLUE;
        }
    }
    
    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();
    public abstract void loseHealth(int damage);
    public abstract int getHealth();
    public abstract void moveUp();
    public abstract void moveDown();
    public abstract void resourceInflux(int type);

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isPlayerLeft() {
        return playerLeft;
    }

    public int getGold() {
        return gold;
    }

    public void reduceGold(int gold) {
        this.gold -= gold;
    }

    public int getTimber() {
        return timber;
    }

    public void reduceTimber(int timber) {
        this.timber -= timber;
    }
    
    public boolean checkCost(int goldCost, int timberCost) {
        for (GameObject object : handler.object) {
            if (object.getId() == ID.Cursor
                    && object.isPlayerLeft() == isPlayerLeft()) {
                if (object.getGold() >= goldCost
                        && object.getTimber() >= timberCost) {
                    object.reduceGold(goldCost);
                    object.reduceTimber(timberCost);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }
}